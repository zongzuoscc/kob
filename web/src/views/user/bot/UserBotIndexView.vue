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
                                        <h1 class="modal-title fs-5" id="exampleModalLabel">创建Bot</h1>
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
                                            <textarea v-model="botadd.content" class="form-control" id="add-bot-code" rows="7" placeholder="请编写Bot代码"></textarea>
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
                                            <button type="button" class="btn btn-sm btn-secondary" style="margin-right: 10px;">管理</button>
                                            <button type="button" class="btn btn-sm btn-danger">删除</button>
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
import { ref,reactive } from "vue";
import $ from "jquery";
import { useStore } from "vuex";
import { Modal } from "bootstrap/dist/js/bootstrap";

export default {
    setup(){
        const store = useStore();
        let bots = ref([]);

        const botadd=reactive({
            title: "",
            description: "",
            content: "",
            error_message: ""
        });

        const refresh_bots = () => {
            $.ajax({
                url: "http://localhost:3000/user/bot/getlist",
                method: "GET",
                headers:{
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp){
                    bots.value = resp;
                }
            })
        };

        refresh_bots();

        const add_bot = () =>{
            botadd.error_message = "";
            $.ajax({
                url: "http://localhost:3000/user/bot/add",
                method: "POST",
                data:{
                    title: botadd.title,
                    description: botadd.description,
                    content: botadd.content,
                },
                headers:{
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp){
                    if(resp.error_message=== "success"){
                        botadd.title = "";
                        botadd.description = "";
                        botadd.content = "";
                        Modal.getInstance("#add-bot-btn").hide();
                        refresh_bots();
                    }else{
                        botadd.error_message = resp.error_message;
                    }
                }
            })
        }

        return {
            bots,
            botadd,
            add_bot,
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