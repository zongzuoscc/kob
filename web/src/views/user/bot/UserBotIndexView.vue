<template>
    <div class="container">
        <div class="row">
            <div class="col-3">
                <div class="card" style="margin-top: 20px;">
                    <div class="card-body">
                        <div style="text-align: center;">
                            <img :src="$store.state.user.photo" alt="用户头像" class="user-photo">
                        </div>
                        <hr>
                        <div style="font-size: 120%; font-weight: bold; text-align: center;">{{ $store.state.user.username }}</div>
                        
                        <button type="button" class="btn btn-warning" style="width: 100%; margin-top: 10px;" data-bs-toggle="modal" data-bs-target="#update-info-modal">
                            修改信息
                        </button>
                    </div>
                </div>
            </div>

            <div class="modal fade" id="update-info-modal" tabindex="-1">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">修改用户信息</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <div class="mb-3">
                                <label for="update-username" class="form-label">用户名</label>
                                <input v-model="userInfo.username" type="text" class="form-control" id="update-username" placeholder="请输入新用户名">
                            </div>
                            <div class="mb-3">
                                <label for="update-photo" class="form-label">头像</label>
                                <div class="input-group">
                                    <input v-model="userInfo.photo" type="text" class="form-control" id="update-photo" placeholder="头像URL (可直接填写或点击上传)">
                                    <input type="file" id="upload-file-input" style="display: none;" @change="upload_param_img" accept="image/*">
                                    <button class="btn btn-outline-secondary" type="button" @click="trigger_upload">上传</button>
                                </div>
                                <div style="margin-top: 10px; text-align: center;" v-if="userInfo.photo">
                                    <img :src="userInfo.photo" alt="预览" style="width: 100px; height: 100px; border-radius: 50%; object-fit: cover;">
                                </div>
                            </div>
                            <hr>
                            <div class="alert alert-info" role="alert" style="padding: 5px 10px; font-size: 14px;">
                                如需修改密码，请填写以下字段，否则请留空。
                            </div>
                            <div class="mb-3">
                                <label for="update-old-password" class="form-label">原密码</label>
                                <input v-model="userInfo.old_password" type="password" class="form-control" id="update-old-password" placeholder="修改密码时必填">
                            </div>
                            <div class="mb-3">
                                <label for="update-password" class="form-label">新密码</label>
                                <input v-model="userInfo.password" type="password" class="form-control" id="update-password" placeholder="请输入新密码">
                            </div>
                            <div class="mb-3">
                                <label for="update-password-confirm" class="form-label">确认新密码</label>
                                <input v-model="userInfo.confirmed_password" type="password" class="form-control" id="update-password-confirm" placeholder="请再次输入新密码">
                            </div>
                            <div class="error_message" style="color: red;">{{ userInfo.error_message }}</div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                            <button type="button" class="btn btn-primary" @click="update_info">保存修改</button>
                        </div>
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
import { ref, reactive, onMounted } from "vue";
import $ from "jquery";
import { useStore } from "vuex";
import { Modal } from "bootstrap/dist/js/bootstrap";
import { VAceEditor } from "vue3-ace-editor";
import ace from "ace-builds/src-noconflict/ace";
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

        // ================== 用户信息更新 & 头像上传逻辑 ==================
        const userInfo = reactive({
            username: "",
            photo: "",
            old_password: "",
            password: "",
            confirmed_password: "",
            error_message: "",
        });

        // 页面加载时，回显当前的用户名和头像
        onMounted(() => {
            userInfo.username = store.state.user.username;
            userInfo.photo = store.state.user.photo;
        });

        // 触发隐藏的文件选择框
        const trigger_upload = () => {
            document.getElementById("upload-file-input").click();
        }

        // 处理文件上传
        const upload_param_img = (e) => {
            const file = e.target.files[0];
            if (!file) return;

            let formData = new FormData();
            formData.append("file", file);

            $.ajax({
                url: "http://127.0.0.1:3000/api/user/account/photo/upload",
                type: "POST",
                data: formData,
                contentType: false,
                processData: false,
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        userInfo.photo = resp.url; // 将 OSS 返回的 URL 填入表单
                    } else {
                        userInfo.error_message = resp.error_message;
                    }
                }
            });
        }

        // 提交修改信息
        const update_info = () => {
            userInfo.error_message = "";
            $.ajax({
                url: "http://127.0.0.1:3000/api/user/account/update",
                method: "POST",
                data: {
                    username: userInfo.username,
                    photo: userInfo.photo,
                    old_password: userInfo.old_password, // 只有改密码才需要，后端会判空
                    password: userInfo.password,
                    confirmed_password: userInfo.confirmed_password,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        // 更新 Vuex 状态
                        store.commit("updateUser", {
                            ...store.state.user,
                            username: userInfo.username,
                            photo: userInfo.photo,
                        });
                        // 隐藏模态框
                        Modal.getInstance("#update-info-modal").hide();
                        // 清空密码输入框，防止下次误操作
                        userInfo.old_password = "";
                        userInfo.password = "";
                        userInfo.confirmed_password = "";
                    } else {
                        userInfo.error_message = resp.error_message;
                    }
                }
            });
        };
        // ==========================================================


        // ================== Bot 增删改查逻辑 (保持原有) ==================
        const initialBotCode = `//下面是bot的示例代码，用户可以在此基础上进行修改，目前只支持java编程语言
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
            content: initialBotCode,
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
            // 新增的返回
            userInfo,
            trigger_upload,
            upload_param_img,
            update_info,
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

/* 修改点2：添加正圆形头像样式 */
img.user-photo {
    width: 15vh;        /* 宽度固定 */
    height: 15vh;       /* 高度与宽度一致 */
    border-radius: 50%; /* 圆形 */
    object-fit: cover;  /* 填充模式：裁剪多余部分，保证不拉伸 */
    margin-bottom: 10px;
}
</style>