<template>
    <div class="container">
        <div class="row">
            <div class="col-3">
                <div class="card" style="margin-top: 20px;">
                    <div class="card-body">
                        <img :src="$store.state.user.photo"  alt="Bot Image" style="width: 100%;">
                    </div>
                </div>
            </div>
            <div class="col-9">
                <div class="card" style="margin-top: 20px;">
                    <div class="card-body">
                        <div class="card-header">
                            <span style="font-size: 130%;">我的Bot</span>
                            <button type="button" class="btn btn-primary float-end" data-bs-toggle="modal" data-bs-target="#add-bot-btn">
                                创建Bot
                            </button>
                            <!-- Modal -->
                            <div class="modal fade" id="add-bot-btn" tabindex="-1">
                                <div class="modal-dialog modal-xl">
                                    <div class="modal-content">
                                    <div class="modal-header">
                                        <h1 class="modal-title fs-5">创建Bot</h1>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <div class="mb-3">
                                            <label for="add-bot-title" class="form-label">Bot名称</label>
                                            <input v-model="botadd.title" type="text" class="form-control" id="add-bot-title" placeholder="请输入Bot名称">
                                        </div>
                                        <div class="mb-3">
                                            <label for="add-bot-description" class="form-label">Bot简介</label>
                                            <textarea v-model="botadd.description" class="form-control" id="add-bot-description" rows="3" placeholder="请输入Bot描述"></textarea>
                                        </div>
                                        <div class="mb-3">
                                            <label for="add-bot-code" class="form-label">Bot代码</label>
                                            <VAceEditor
                                                v-model:value="botadd.content"
                                                @init="editorInit"
                                                lang="c_cpp"
                                                theme="textmate"
                                                style="height: 300px" />
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <div class="error_message">{{ botadd.error_message }}</div>
                                        <button type="button" class="btn btn-primary" @click="add_bot">创建</button>
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                                    </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="card-body">
                            <table class="table table-dark table-hover">
                                <thead>
                                    <tr>
                                        <th>Bot名称</th>
                                        <th>创建时间</th>
                                        <th>操作</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr v-for="bot in bots" :key="bot.id">
                                        <td>{{ bot.title }}</td>
                                        <td>{{ bot.createtime }}</td>
                                        <td>
                                            <button type="button" class="btn btn-sm btn-secondary" style="margin-right: 10px;" data-bs-toggle="modal" :data-bs-target="'#update-bot-modal-' + bot.id">修改</button>
                                            <button type="button" class="btn btn-sm btn-danger" @click="remove_bot(bot)">删除</button>

                                            <div class="modal fade" :id="'update-bot-modal-' + bot.id" tabindex="-1">
                                                <div class="modal-dialog modal-xl">
                                                    <div class="modal-content">
                                                    <div class="modal-header">
                                                        <h1 class="modal-title fs-5">修改Bot</h1>
                                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                    </div>
                                                    <div class="modal-body">
                                                        <div class="mb-3">
                                                            <label for="add-bot-title" class="form-label">Bot名称</label>
                                                            <input v-model="bot.title" type="text" class="form-control" id="add-bot-title" placeholder="请输入Bot名称">
                                                        </div>
                                                        <div class="mb-3">
                                                            <label for="add-bot-description" class="form-label">Bot简介</label>
                                                            <textarea v-model="bot.description" class="form-control" id="add-bot-description" rows="3" placeholder="请输入Bot描述"></textarea>
                                                        </div>
                                                        <div class="mb-3">
                                                            <label for="add-bot-code" class="form-label">Bot代码</label>
                                                            <VAceEditor
                                                                v-model:value="bot.content"
                                                                @init="editorInit"
                                                                lang="c_cpp"
                                                                theme="textmate"
                                                                style="height: 300px" />
                                                        </div>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <div class="error_message">{{ bot.error_message }}</div>
                                                        <button type="button" class="btn btn-primary" @click="update_bot(bot)">保存修改</button>
                                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                                                    </div>
                                                    </div>
                                                </div>
                                            </div>

                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div> 
    </div>
</template>

<script>
import { ref, reactive } from "vue";
import $ from "jquery";
import { useStore } from "vuex";
import { Modal } from "bootstrap/dist/js/bootstrap";
import { VAceEditor } from "vue3-ace-editor";
import ace from "ace-builds/src-noconflict/ace";
// 修改点1: 引入 Java 模式，配合你的 Java 代码
import "ace-builds/src-noconflict/mode-java"; 
import "ace-builds/src-noconflict/theme-textmate";

export default {
    components: {
        VAceEditor,
    },
    setup() {
        ace.config.set(
            "basePath",
            "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/"
        );
        
        const store = useStore();
        let bots = ref([]);

        // 修改点2: 定义默认的 Bot 代码字符串
        const initialBotCode = `//下面是bot的示例代码，用户可以在此基础上进行修改
package cumt.kob.botrunningsystem.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Bot implements java.util.function.Supplier<Integer>{

    static class Cell{
        public int x,y;
        public Cell(int x,int y){
            this.x = x;
            this.y = y;
        }
    }

    private boolean check_tail_increasing(int step){//检验当前回合蛇的长度是否增加
        if(step <= 10) return true;
        return step%3 == 1;
    }

    public List<Cell> getCells(int sx,int sy,String steps){
        steps = steps.substring(1,steps.length()-1);

        List<Cell> res =  new ArrayList<>();

        int[] dx={-1,0,1,0};
        int[] dy={0,1,0,-1};

        int x = sx;
        int y = sy;
        int step = 0;
        res.add(new Cell(x,y));
        for(int i=0;i<steps.length();i++){
            int d=steps.charAt(i)-'0';
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x,y));
            if(!check_tail_increasing(++step)){
                res.remove(0);
            }
        }
        return res;
    }

    public Integer nextMove(String input) {
        String[] strs = input.split("#");
        int[][] g = new int[13][14];
        for(int i=0,k=0;i<13;i++){
            for(int j=0;j<14;j++,k++){
                if(strs[0].charAt(k)=='1')
                    g[i][j] = 1;
            }
        }

        int aSx = Integer.parseInt(strs[1]);
        int aSy = Integer.parseInt(strs[2]);
        int bSx = Integer.parseInt(strs[4]);
        int bSy = Integer.parseInt(strs[5]);

        List<Cell> aCells = getCells(aSx, aSy, strs[3]);
        List<Cell> bCells = getCells(bSx, bSy, strs[6]);

        for (Cell c : aCells) {
            g[c.x][c.y] = 1;
        }
        for (Cell c : bCells) {
            g[c.x][c.y] = 1;
        }
        int[] dx={-1,0,1,0};
        int[] dy={0,1,0,-1};
        for (int i = 0; i < 4; i++) {
            int x = aCells.get(aCells.size()-1).x + dx[i];
            int y = aCells.get(aCells.size()-1).y + dy[i];
            if(x >= 0 && x < 13 && y >= 0 && y < 14 && g[x][y] == 0){//判断越界
                return i;
            }
        }
        return 0;
    }

    @Override
    public Integer get() {
        File file = new File("input.txt");
        try {
            Scanner sc = new Scanner(file);
            return nextMove(sc.next());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}`;

        const botadd = reactive({
            title: "",
            description: "",
            content: initialBotCode, // 修改点3: 初始化时使用默认代码
            error_message: ""
        });

        const refresh_bots = () => {
            $.ajax({
                url: "http://127.0.0.1:3000/api/user/bot/getlist",
                method: "GET",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    bots.value = resp;
                }
            })
        };

        refresh_bots();

        const add_bot = () => {
            botadd.error_message = "";
            $.ajax({
                url: "http://127.0.0.1:3000/api/user/bot/add",
                method: "POST",
                data: {
                    title: botadd.title,
                    description: botadd.description,
                    content: botadd.content,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        botadd.title = "";
                        botadd.description = "";
                        // 修改点4: 创建成功后，重置回默认代码，而不是清空
                        botadd.content = initialBotCode; 
                        Modal.getInstance("#add-bot-btn").hide();
                        refresh_bots();
                    } else {
                        botadd.error_message = resp.error_message;
                    }
                }
            })
        }

        const update_bot = (bot) => {
            botadd.error_message = "";
            $.ajax({
                url: "http://127.0.0.1:3000/api/user/bot/update",
                method: "POST",
                data: {
                    bot_id: bot.id,
                    title: bot.title,
                    description: bot.description,
                    content: bot.content,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        Modal.getInstance('#update-bot-modal-' + bot.id).hide();
                        refresh_bots();
                    } else {
                        bot.error_message = resp.error_message;
                    }
                }
            })
        }

        const remove_bot = (bot) => {
            $.ajax({
                url: "http://127.0.0.1:3000/api/user/bot/remove",
                method: "POST",
                data: {
                    bot_id: bot.id,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        refresh_bots();
                    }
                }
            })
        }

        return {
            bots,
            botadd,
            add_bot,
            update_bot,
            remove_bot,
        }
    }
};
</script>

<style scoped>
div.error_message {
    color: red;
    float: left;
    line-height: 38px;
}
</style>