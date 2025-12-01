import { AcGameObject } from "./AcGameObject";
import { TheCell } from "./TheCell.js";

export class Snake extends AcGameObject{
    constructor(info, gamemap){
        super();

        this.id = info.id;
        this.color = info.color;
        this.gamemap = gamemap;
        
        this.cells = [new TheCell(info.r, info.c)]; //存放蛇的身体，cells[0]存放蛇头
        this.next_cell = null; //下一步的目标位置

        this.speed = 5; //蛇每秒钟走多少格子
        this.direction = -1; //-1表示没有指令，0表示上，1表示右，2表示下，3表示左
        this.status = "idle"; //idle表示静止，move表示移动，die表示死亡

        this.dr=[-1,0,1,0]; //方向向量，上右下左
        this.dc=[0,1,0,-1]; //方向向量，上右下左

        this.step=0; //表示当前回合数
        this.eps=1e-2;//允许的误差
    }

    start(){

    }

    set_direction(d){//设置蛇的移动方向
        this.direction = d;
    }

    check_tail_increasing(){//检查当前回合蛇是否应该增加长度
        if(this.step <= 10) return true;
        if(this.step % 3 === 1) return true;
        return false;
    }

    next_step(){//将蛇的状态变为走下一步
        const d = this.direction;
        this.next_cell=new TheCell(this.cells[0].r + this.dr[d], this.cells[0].c + this.dc[d]);
        this.direction = -1; //清空操作
        this.status = "move";
        this.step++; //回合数增加

        const k=this.cells.length;
        for(let i=k;i>0;i--){//身体后面的部分依次向前移动一格
            this.cells[i]=JSON.parse(JSON.stringify(this.cells[i-1]));//深拷贝
        }
    }

    update_move(){
        
        const dx = this.next_cell.x - this.cells[0].x;
        const dy = this.next_cell.y - this.cells[0].y;
        const distance = Math.sqrt(dx * dx + dy * dy);

        if(distance <= this.eps){//到达目标点
            this.cells[0] = this.next_cell; //更新蛇头位置 ,添加一个新蛇头
            this.next_cell = null;
            this.status="idle";//停止移动

            if(!this.check_tail_increasing()){//蛇不变长
                this.cells.pop();//删除蛇尾
            }
        }else{
            const move_distance = this.speed * this.timedelta / 1000; //每两帧之间移动的距离
            this.cells[0].x += move_distance * dx / distance;
            this.cells[0].y += move_distance * dy / distance;
            if(!this.check_tail_increasing()){//蛇不变长
                const k = this.cells.length;
                const tail = this.cells[k-1],tail_target = this.cells[k-2];
                const tail_dx=tail_target.x - tail.x;
                const tail_dy=tail_target.y - tail.y;
                tail.x += move_distance * tail_dx / distance;
                tail.y += move_distance * tail_dy / distance;
            }
        }
    }

    update(){//每一帧执行一次
        if(this.status === "move"){
            this.update_move();
        }

        this.render();
    }

    render(){
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;

        ctx.fillStyle = this.color;
        for(const cell of this.cells){
            ctx.beginPath();
            ctx.arc(cell.x*L, cell.y*L, L/2 * 0.8, 0, Math.PI*2);
            ctx.fill();
        }
        for(let i=1;i<this.cells.length;i++){
            const a =this.cells[i-1], b=this.cells[i];
            if(Math.abs(a.x - b.x) < this.eps && Math.abs(a.y - b.y) < this.eps) continue;
            if(Math.abs(a.x - b.x) < this.eps){//同列
                ctx.fillRect((a.x-0.4)*L, Math.min(a.y, b.y)*L, L*0.8, Math.abs(a.y - b.y)*L);    
            }else{
                ctx.fillRect(Math.min(a.x, b.x)*L, (a.y-0.4)*L, Math.abs(a.x - b.x)*L, L*0.8); 
            }
            
        }
    }
}
