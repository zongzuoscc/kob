const AC_GAME_OBJECTS = [];

export class AcGameObject {
    constructor() {
        AC_GAME_OBJECTS.push(this);
        this.timedelta = 0; //两帧之间的时间间隔
        this.has_called_start = false; //是否执行过start函数
    }

    start() { //只执行一次

    }

    update() { //每一帧执行一次,除了第一帧

    }

    on_destroy() { //对象被销毁之前执行

    }

    destroy() {
        this.on_destroy();
        for(let i in AC_GAME_OBJECTS) {
            const obj = AC_GAME_OBJECTS[i];
            if(obj === this) {
                AC_GAME_OBJECTS.splice(i);
                break;
            }
        }
    }
}


let last_timestamp; //上一次执行的时间点
const step =timestamp => {
    //of遍历的是值，in遍历的是索引
    for(let obj of AC_GAME_OBJECTS){
        if(!obj.has_called_start){
            obj.has_called_start = true;
            obj.start();
        }else{
            obj.timedelta = timestamp - last_timestamp;
            obj.update();
        }
    } 

    last_timestamp = timestamp;
    requestAnimationFrame(step);
}

requestAnimationFrame(step)