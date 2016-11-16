function getSeq(seq){
    let charArray = ['A', 'B', 'C', 'D',
		'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S',
		'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];
	let nowDate = new Date();
	var date = (""+nowDate.getFullYear()).substring(2) + nowDate.getMonth() + nowDate.getDate();
    let name = seq.name;
    db.sequenceId.update({_id:seq.name},{$inc: {seq:1}});
    let newSeq = db.sequenceId.findOne({_id:seq.name});
    let startNum = seq.startNum;
    let seqNo = startNum;
    if(!newSeq){
        newSeq = {
            _id:seq.name,
            seq:seq.startNum
        };
        db.sequenceId.save(newSeq);
    }else{
        seqNo = newSeq.seq;
    }
    
    let jobNo = seqNo % (charArray.length * startNum);
    let charArrayIndex = parseInt(jobNo / startNum);
    let str = seq.prefix + date + charArray[charArrayIndex];
    str+=((jobNo % startNum + startNum)+"").substring(1);
    return str;
}

getSeq({startNum:10000000,prefix:"SCY",name:"SystemCategory"});



//init data
function initData(userNo,name,code,describe){
    let nowDate  = new Date();
    //初始化当前默认系统 pm?hx?fx
    let systemCategory = {
            code:code,
            name:name,
            describe:describe,
            valid:0,
            createDate:nowDate,
            createUser:userNo
    }
    if(!db.boSystemCategory.findOne({code:code})){
        //初始化系统
        db.sequenceId.find({_id:"SystemCategory"});
        systemCategory["_id"] = getSeq({startNum:10000000,prefix:"SCY",name:"SystemCategory"});
        db.boSystemCategory.save(systemCategory);
        console.log("成功新增系统....")
        //刷新角色
        db.boRole.update({},{$set:{systemCategory:code}},false,true);
        console.log("成功刷新角色....");
        //更新用户角色
        let users = [];
        db.boUser.find({}).forEach((user)=> { 
              if(user.role){
                  var role = db.boRole.findOne({_id:user.role._id});
                  user.role = role;
                  //记录user 不直接在forEach中修改 因为forEach/update中特性冲突
                  users.push(user);
              }
        });
        for(let i = 0;i<users.length;i++){
            db.boUser.update({_id:users[i]._id},{$set:{role:users[i].role}});
        }
        console.log("成功刷新用户角色....");
    }
    
    
    //初始化菜单
    //获取根菜单
    let rootMenu = db.boMenu.findOne({code:"system_setting"});
    let menu = {
        parentMenuId:rootMenu._id,
        code:"system_category",
        type:0,
        valid:1,
        nameCN:"系统分类",
        nameEN:"SystemCategory",
        nameTW:"系统分类",
        sort:99,
        url:"systemCategoryController/index.do",
        createDate:nowDate,
        createUser:userNo,
        status:0
    }
    if(!db.boMenu.findOne({code:menu.code})){
        menu["_id"] = getSeq({startNum:1000000,prefix:"M",name:"Menu"});
        db.boMenu.save(menu);
        console.log("成功新建菜单....");
    }
    
    
     //初始化超级管理系统
     systemCategory = {
            code:"super_admin",
            name:"超级系统",
            describe:"超级系统，用于所有系统的管理。",
            valid:0,
            createDate:new Date(),
            createUser:userNo
    }
    if(!db.boSystemCategory.findOne({code:systemCategory.code})){
         systemCategory["_id"] = getSeq({startNum:10000000,prefix:"SCY",name:"SystemCategory"});
         db.boSystemCategory.save(systemCategory);
         console.log("成功新建超级系统....");
    }
    
    
     //给超级管理系统 初始化个超级角色 并且设置userNo拥有此角色
    let role = {
        systemCategory:systemCategory.code,
        roleNo:systemCategory.code+"_admin",
        roleName:"系统管理员",
        remark:"系统超级管理员",
        valid:1,
        status:0,
        createDate:nowDate,
        createUser:userNo
    }
    if(!db.boRole.findOne({roleNo:role.roleNo})){
        role["_id"] = getSeq({startNum:1000000,prefix:"R",name:"Role"});
        db.boRole.save(role);
        //设置user拥有此角色
        db.boUser.update({userNo:userNo},{$set:{role:role}});
        console.log("成功设置超级系统权限....");
    }
    
    
    //给所有菜单设置超级管理员权限
    if(role["_id"]){
        db.boMenu.update({},{$addToSet: {roleList:{_id:role["_id"]}}},false,true);
    }
    
    console.log("init done.....");
}


//arg1:userNo 用于初始化的admin账号
//arg2:公司名称  arg3:公司编码   arg4:公司描述
initData("superadmin","PM直播间","studio","PM直播间");