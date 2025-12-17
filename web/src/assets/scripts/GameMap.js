import { AcGameObject } from "./AcGameObject.js";
import { Wall } from "./Wall.js";
import { Snake } from "./Snake.js";

export class GameMap extends AcGameObject {
    constructor(ctx, parent,store) {
        super();

        this.ctx = ctx;
        this.parent = parent;
        this.store = store;
        this.L=0;

        this.rows = 13;
        this.cols = 14;

        this.inner_walls_count = 20; //地图中间的墙数目
        this.walls = [];

        this.snakes = [
            new Snake({id : 0, color: "#4876EC", r: this.rows - 2, c: 1}, this),
            new Snake({id : 1, color: "#F94848", r: 1, c: this.cols - 2}, this),
        ];
    }

    //已经在后端实现了
    // check_connectivity(g,sx,sy,tx,ty){
    //     if(sx == tx && sy == ty) return true;
    //     g[sx][sy] = true;

    //     let dx=[-1,0,1,0];
    //     let dy=[0,1,0,-1];
    //     for(let i = 0;i < 4;i ++ ) {
    //         let x = sx + dx[i], y = sy + dy[i];
    //         if(!g[x][y] && this.check_connectivity(g,x,y,tx,ty)) {
    //             return true;
    //         }
    //     }
    //     return false;
    // }

    create_walls(){
        const g=this.store.state.pk.gamemap;

        for(let r = 0; r < this.rows; r ++ ) {
            for(let c = 0; c < this.cols; c ++ ) {
                if(g[r][c]) {
                    this.walls.push(new Wall(r, c, this));
                }
            }
        }

        // return true;
    }

    add_listening_events(){
        this.ctx.canvas.focus();

        this.ctx.canvas.addEventListener("keydown", e => {
            let d = -1;
            if(e.key === 'w') {
                d = 0;
            }else if(e.key === 'd') {
                d = 1;
            }else if(e.key === 's') {
                d = 2;
            }else if(e.key === 'a') {
                d = 3;
            }

            if(d >= 0){//操作合法则向后端发送移动的请求
                this.store.state.pk.socket.send(JSON.stringify({
                    event: "move",
                    direction: d, 
                }))
            }
        });
    }

    start() {
    //     for(let i = 0; i < 1000; i ++ ) {
    //         if(this.create_walls()) break;
    //     }
        this.create_walls();
        this.add_listening_events();
    }

    update_size(){
        this.L = parseInt(Math.min(this.parent.clientWidth/this.cols, this.parent.clientHeight/this.rows));
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }

    check_ready(){//检查两条蛇是否都准备好开始下一回合
        for(const snake of this.snakes){
            if(snake.status !== "idle") return false;
            if(snake.direction === -1) return false;
        }
        return true;
    }

    next_step(){//将两条蛇的状态都变为走下一步
        for(const snake of this.snakes){
            snake.next_step();
        }
    }

    check_valid(cell){//检测目标位置是否合法，没有撞到蛇的身体或者墙
        for(const wall of this.walls){
            if(wall.r === cell.r && wall.c === cell.c){
                return false;
            }
        }
        for(const snake of this.snakes){
            let k=snake.cells.length;
            if(!snake.check_tail_increasing()){//当蛇尾会移动时，不用检查蛇尾
                k--;
            }
            for(let i=0;i<k;i++){
                if(snake.cells[i].r === cell.r && snake.cells[i].c === cell.c){
                    return false;
                }
            }
        }
        return true;
    }

    update() {
        this.update_size();
        if(this.check_ready()) {
            this.next_step();
        }
        this.render();
    }

    render(){
        const color_even = "#AAD751";
        const color_odd = "#A2D149";
        for (let r = 0; r < this.rows; r ++ ) {
            for (let c = 0; c < this.cols; c ++ ) {
                if ((r + c) % 2 == 0) {
                    this.ctx.fillStyle = color_even;
                } else {
                    this.ctx.fillStyle = color_odd;
                }
                this.ctx.fillRect(c * this.L, r * this.L, this.L, this.L);
            } 
        }
    }

}