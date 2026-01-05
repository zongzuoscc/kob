<template>
    <ContentField>
        <table class="table table-dark table-hover" style="text-align: center;">
            <thead>
                <tr>
                    <th>玩家</th>
                    <th>积分</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="user in users" :key="user.id">
                    <td>
                        <img :src="user.photo" alt="" class="record-user-photo">
                        &nbsp;
                        <span class="record-user-username">{{ user.username }}</span>
                    </td>
                    <td>{{ user.rating }}</td>
                </tr>
            </tbody>
        </table>
        <nav aria-label="...">
        <ul class="pagination" style="float: right;">
            <li class="page-item" @click="click_page(-2)">
                <a class="page-link" href="#">前一页</a>
            </li>
            <li :class="'page-item ' + page.is_active" v-for="page in pages" :key="page.number" @click="click_page(page.number)">
                <a class="page-link" href="#">{{ page.number }}</a>
            </li>
            <li class="page-item" @click="click_page(-1)">
                <a class="page-link" href="#">后一页</a>
            </li>
        </ul>
        </nav>

    </ContentField>
</template>

<script>
import ContentField from '../../components/ContentField.vue'
import { useStore } from 'vuex';
import { ref } from 'vue';
import $ from 'jquery';

export default {
    components: {
        ContentField
    },
    setup() {
        const store = useStore();
        let users = ref([]);
        let current_page = 1;
        let total_users = 0;
        let pages = ref([]);
        

        const click_page = page => {
            if (page === -2) page = current_page - 1;
            else if (page === -1) page = current_page + 1;
            let max_pages = parseInt(Math.ceil(total_users / 10));

            if (page >= 1 && page <= max_pages) {
                pull_page(page);
            }
        }

        const udpate_pages = () => {
            let max_pages = parseInt(Math.ceil(total_users / 10));
            let new_pages = [];
            for (let i = current_page - 2; i <= current_page + 2; i ++ ) {
                if (i >= 1 && i <= max_pages) {
                    new_pages.push({
                        number: i,
                        is_active: i === current_page ? "active" : "",
                    });
                }
            }
            pages.value = new_pages;
        }

        const pull_page = page => {
            current_page = page;
            $.ajax({
                url: "http://127.0.0.1:3000/ranklist/getlist",
                data: {
                    page,
                },
                type: "get",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    users.value = resp.users;
                    total_users = resp.users_count;
                    udpate_pages();
                },
                error(resp) {
                    console.log(resp);
                }
            })
        }

        pull_page(current_page);


        return {
            users,
            pages,
            click_page
        }
    }
}
</script>

<style scoped>
img.record-user-photo {
    width: 4vh;
    border-radius: 50%;
}

/* --- 核心修复代码 --- */

/* 1. 强制将最外层卡片背景设为半透明黑色 */
:deep(.content-field .card) {
    background-color: rgba(0, 0, 0, 0.75) !important; 
    color: white !important;
    border: 1px solid #444; 
}

/* 2. 这里的关键：如果有任何漏网的白色背景，全部设为透明 */
:deep(.content-field .card .card-body),
:deep(.content-field .card .card) {
    background-color: transparent !important;
}

/* 3. 表格背景设为透明，这样才能透出卡片的黑色背景 */
:deep(.table) {
    --bs-table-bg: transparent !important; 
    --bs-table-color: white !important; /* 强制文字白色 */
}

/* 4. 修复表格 Hover 时的颜色（避免 hover 时依然看不清） */
:deep(.table-hover > tbody > tr:hover > *) {
    --bs-table-accent-bg: rgba(255, 255, 255, 0.1) !important; /* 鼠标悬停变亮一点点 */
    color: white !important;
}

/* 5. 分页器美化（黑底白字） */
:deep(.page-link) {
    background-color: #212529 !important; 
    border-color: #444 !important;
    color: #ccc !important;
}

:deep(.page-link:hover) {
    background-color: #3c4046 !important;
    color: white !important;
}

:deep(.page-item.active .page-link) {
    background-color: #0d6efd !important;
    border-color: #0d6efd !important;
    color: white !important;
}

:deep(.page-item.disabled .page-link) {
    background-color: #1a1a1a !important;
    border-color: #333 !important;
    color: #666 !important;
}

</style>
