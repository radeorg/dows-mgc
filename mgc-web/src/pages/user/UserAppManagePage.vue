<template>
  <div id="userAppManagePage">
    <!-- 用户应用管理页面标题 -->
    <div class="page-header">
      <h2>我的应用</h2>
      <p>管理您创建的所有应用</p>
    </div>

    <!-- 搜索表单 - 简化版，移除创建者搜索 -->
    <a-form layout="inline" :model="searchParams" @finish="doSearch">
      <a-form-item label="应用名称">
        <a-input v-model="searchParams.appName" placeholder="输入应用名称" />
      </a-form-item>
      <a-form-item label="生成类型">
        <a-select
          v-model="searchParams.codeGenType"
          placeholder="选择生成类型"
          style="width: 150px"
        >
          <a-select-option value="">全部</a-select-option>
          <a-select-option
            v-for="option in CODE_GEN_TYPE_OPTIONS"
            :key="option.value"
            :value="option.value"
          >
            {{ option.label }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit">搜索</a-button>
      </a-form-item>
    </a-form>
    <a-divider />

    <!-- 统计信息 -->
    <div class="stats-container">
      <a-row :gutter="16">
        <a-col :span="6">
          <a-card>
            <a-statistic title="总应用数" :value="total" />
          </a-card>
        </a-col>
        <a-col :span="6">
          <a-card>
            <a-statistic title="已部署应用" :value="deployedCount" />
          </a-card>
        </a-col>
        <a-col :span="6">
          <a-card>
            <a-statistic title="HTML应用" :value="htmlCount" />
          </a-card>
        </a-col>
        <a-col :span="6">
          <a-card>
            <a-statistic title="Vue项目" :value="vueCount" />
          </a-card>
        </a-col>
      </a-row>
    </div>

    <!-- 表格 - 用户版本，移除优先级和创建者列 -->
    <a-table
      :columns="columns"
      :data-source="data"
      :pagination="pagination"
      @change="doTableChange"
      :scroll="{ x: 1000 }"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'appName'">
          <a-button type="link" @click="viewChat(record)" class="app-name-link">
            {{ record.appName || '未命名应用' }}
          </a-button>
        </template>
        <template v-else-if="column.dataIndex === 'cover'">
          <a-image v-if="record.cover" :src="record.cover" :width="80" :height="60" />
          <div v-else class="no-cover">无封面</div>
        </template>
        <template v-else-if="column.dataIndex === 'initPrompt'">
          <a-tooltip :title="record.initPrompt">
            <div class="prompt-text">{{ record.initPrompt }}</div>
          </a-tooltip>
        </template>
        <template v-else-if="column.dataIndex === 'codeGenType'">
          {{ formatCodeGenType(record.codeGenType) }}
        </template>
        <template v-else-if="column.dataIndex === 'deployedTime'">
          <span v-if="record.deployedTime">
            {{ formatTime(record.deployedTime) }}
          </span>
          <span v-else class="text-gray">未部署</span>
        </template>
        <template v-else-if="column.dataIndex === 'createTime'">
          {{ formatTime(record.createTime) }}
        </template>
        <template v-else-if="column.key === 'action'">
          <a-space>
            <a-button type="primary" size="small" @click="editApp(record)"> 编辑 </a-button>
            <a-popconfirm title="确定要删除这个应用吗？" @confirm="deleteApp(record.id)">
              <a-button danger size="small">删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>
  </div>
</template>

<script lang="ts" setup>
import { computed, onMounted, reactive, ref, h } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { listMyAppVoByPage, deleteApp as deleteAppApi } from '@/api/appController'
import { CODE_GEN_TYPE_OPTIONS, formatCodeGenType } from '@/utils/codeGenTypes'
import { formatTime } from '@/utils/time'
import { useLoginUserStore } from '@/stores/loginUser'
import dayjs from 'dayjs'

const router = useRouter()
const loginUserStore = useLoginUserStore()

// 创建通用的带排序按钮的标题组件
const createSortableTitle = (title: string, sortField?: string) => {
  if (!sortField) {
    return title
  }
  return h('div', { style: 'display: flex; align-items: center; justify-content: space-between;' }, [
    h('span', title),
    h('div', { class: 'sort-buttons' }, [
      h('button', {
        class: 'sort-button',
        onClick: () => handleSort(sortField, 'asc'),
        title: `${title}升序`
      }, '▲'),
      h('button', {
        class: 'sort-button',
        onClick: () => handleSort(sortField, 'desc'),
        title: `${title}降序`
      }, '▼')
    ])
  ])
}

// 用户版本的列定义 - 移除优先级和创建者
const columns = [
  {
    title: () => createSortableTitle('ID', 'id'),
    dataIndex: 'id',
    width: 80,
  },
  {
    title: () => createSortableTitle('应用名称', 'appName'),
    dataIndex: 'appName',
    width: 150,
  },
  {
    title: '封面',
    dataIndex: 'cover',
    width: 100,
  },
  {
    title: () => createSortableTitle('初始提示词'),
    dataIndex: 'initPrompt',
    width: 200,
  },
  {
    title: () => createSortableTitle('生成类型'),
    dataIndex: 'codeGenType',
    width: 100,
  },
  {
    title: () => createSortableTitle('部署时间', 'deployedTime'),
    dataIndex: 'deployedTime',
    width: 160,
  },
  {
    title: () => createSortableTitle('创建时间', 'createTime'),
    dataIndex: 'createTime',
    width: 160,
    customRender: ({ record }: { record: any }) => {
      return dayjs(record.createTime).format('YYYY-MM-DD HH:mm')
    }
  },
  {
    title: '操作',
    key: 'action',
    width: 160,
    fixed: 'right',
  },
]

// 数据
const data = ref<API.AppVO[]>([])
const total = ref(0)

// 搜索条件 - 自动添加用户ID过滤
const searchParams = reactive<API.AppQueryRequest>({
  pageNum: 1,
  pageSize: 10,
  userId: loginUserStore.loginUser.id, // 自动过滤当前用户的应用
})

// 统计数据
const deployedCount = computed(() => {
  return data.value.filter(app => app.deployedTime).length
})

const htmlCount = computed(() => {
  return data.value.filter(app => app.codeGenType === 'HTML').length
})

const vueCount = computed(() => {
  return data.value.filter(app => app.codeGenType === 'VUE_PROJECT').length
})

// 排序相关
const currentSort = ref<{ field: string; order: 'asc' | 'desc' } | null>(null)

const handleSort = (field: string, order: 'asc' | 'desc') => {
  currentSort.value = { field, order }
  
  // 执行排序
  data.value.sort((a: any, b: any) => {
    let aValue = a[field]
    let bValue = b[field]
    
    // 处理时间字段
    if (field.includes('Time')) {
      aValue = new Date(aValue || 0).getTime()
      bValue = new Date(bValue || 0).getTime()
    }
    
    // 处理数字字段
    if (typeof aValue === 'string' && !isNaN(Number(aValue))) {
      aValue = Number(aValue)
      bValue = Number(bValue)
    }
    
    if (order === 'asc') {
      return aValue > bValue ? 1 : -1
    } else {
      return aValue < bValue ? 1 : -1
    }
  })
  
  message.success(`已按${field}${order === 'asc' ? '升序' : '降序'}排序`)
}

// 获取数据
const fetchData = async () => {
  try {
    const res = await listMyAppVoByPage({
      ...searchParams,
    })
    if (res.data.data) {
      data.value = res.data.data.records ?? []
      total.value = res.data.data.totalRow ?? 0
    }
  } catch (error) {
    console.error('获取应用列表失败', error)
    message.error('获取应用列表失败')
  }
}

// 搜索
const doSearch = () => {
  searchParams.pageNum = 1
  fetchData()
}

// 表格变化
const doTableChange = (pagination: any) => {
  searchParams.pageNum = pagination.current
  searchParams.pageSize = pagination.pageSize
  fetchData()
}

// 分页配置
const pagination = computed(() => ({
  current: searchParams.pageNum,
  pageSize: searchParams.pageSize,
  total: total.value,
  showSizeChanger: true,
  showQuickJumper: true,
  showTotal: (total: number) => `共 ${total} 个应用`,
}))

// 编辑应用 - 跳转到用户版本的编辑页面
const editApp = (record: API.AppVO) => {
  router.push(`/app/edit/${record.id}`)
}

// 查看对话 - 跳转到应用对话页面
const viewChat = (record: API.AppVO) => {
  router.push(`/app/chat/${record.id}`)
}

// 删除应用
const deleteApp = async (id: number) => {
  try {
    await deleteAppApi({ id })
    message.success('删除成功')
    await fetchData()
  } catch (error) {
    console.error('删除失败', error)
    message.error('删除失败')
  }
}

// 页面加载时获取数据
onMounted(() => {
  fetchData()
  
  // 添加鼠标跟随光效
  const handleMouseMove = (e: MouseEvent) => {
    const { clientX, clientY } = e
    const { innerWidth, innerHeight } = window
    const x = (clientX / innerWidth) * 100
    const y = (clientY / innerHeight) * 100

    document.documentElement.style.setProperty('--mouse-x', `${x}%`)
    document.documentElement.style.setProperty('--mouse-y', `${y}%`)
  }

  document.addEventListener('mousemove', handleMouseMove)

  // 清理事件监听器
  onBeforeUnmount(() => {
    document.removeEventListener('mousemove', handleMouseMove)
  })
})
</script>

<style scoped>
#userAppManagePage {
  padding: 20px;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.95), rgba(240, 247, 255, 0.95));
  backdrop-filter: blur(10px);
  min-height: 100vh;
  position: relative;
  overflow: hidden;
}

/* 鼠标跟随蓝光效 */
#userAppManagePage::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background:
    radial-gradient(
      800px circle at var(--mouse-x, 50%) var(--mouse-y, 50%),
      rgba(74, 144, 226, 0.25) 0%,
      rgba(126, 179, 245, 0.2) 40%,
      transparent 80%
    );
  pointer-events: none;
  animation: lightPulse 8s ease-in-out infinite alternate;
  z-index: 0;
}

.page-header {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 12px;
  margin-bottom: 24px;
  text-align: center;
  padding: 24px;
  border: 1px solid rgba(74, 144, 226, 0.2);
  backdrop-filter: blur(8px);
  box-shadow: 0 4px 12px rgba(74, 144, 226, 0.15);
  position: relative;
  z-index: 1;
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #333;
  font-size: 28px;
  font-weight: 600;
  background: linear-gradient(135deg, #4A90E2, #1E5CA9);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.page-header p {
  margin: 0;
  color: #666;
  font-size: 16px;
}

.stats-container {
  margin-bottom: 24px;
}

.stats-container .ant-card {
  text-align: center;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(74, 144, 226, 0.15);
  border: 1px solid rgba(74, 144, 226, 0.2);
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.9), rgba(240, 247, 255, 0.9));
  backdrop-filter: blur(8px);
  position: relative;
  z-index: 1;
}

/* 表单样式 */
:deep(.ant-form) {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 16px;
  border: 1px solid rgba(74, 144, 226, 0.2);
  backdrop-filter: blur(8px);
  box-shadow: 0 2px 8px rgba(74, 144, 226, 0.1);
}

:deep(.ant-form-item-label > label) {
  color: var(--text-primary, #333);
  font-weight: 500;
}

:deep(.ant-input) {
  border-radius: 8px;
  border-color: rgba(74, 144, 226, 0.3);
  transition: all 0.3s ease;
  backdrop-filter: blur(5px);
}

:deep(.ant-input:focus) {
  border-color: #4A90E2;
  box-shadow: 0 0 8px rgba(74, 144, 226, 0.2);
}

:deep(.ant-select) {
  border-radius: 8px;
}

:deep(.ant-select .ant-select-selector) {
  border-radius: 8px;
  border-color: rgba(74, 144, 226, 0.3);
  transition: all 0.3s ease;
}

:deep(.ant-select-focused .ant-select-selector) {
  border-color: #4A90E2 !important;
  box-shadow: 0 0 8px rgba(74, 144, 226, 0.2) !important;
}

/* 表格样式 */
:deep(.ant-table-wrapper) {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  overflow: hidden;
  border: 2px solid rgba(74, 144, 226, 0.3);
  backdrop-filter: blur(12px);
  box-shadow: 0 8px 24px rgba(74, 144, 226, 0.2);
  position: relative;
  z-index: 1;
  margin-top: 24px;
}

:deep(.ant-table-thead > tr > th) {
  background: linear-gradient(135deg, rgba(74, 144, 226, 0.15), rgba(126, 179, 245, 0.1));
  border-bottom: 3px solid rgba(74, 144, 226, 0.4);
  font-weight: 700;
  color: #1E5CA9;
  padding: 20px 16px;
  font-size: 14px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  position: relative;
  overflow: hidden;
}

:deep(.ant-table-tbody > tr:hover > td) {
  background: linear-gradient(135deg, rgba(240, 247, 255, 0.9), rgba(225, 240, 255, 0.8)) !important;
  transform: scale(1.01) !important;
  transition: all 0.3s ease !important;
  border-color: rgba(74, 144, 226, 0.3) !important;
}

:deep(.ant-table-tbody > tr > td) {
  border-bottom: 1px solid rgba(74, 144, 226, 0.15) !important;
  padding: 20px 16px !important;
  vertical-align: middle;
  transition: all 0.3s ease;
}

.no-cover {
  width: 80px;
  height: 60px;
  background: linear-gradient(135deg, #F0F7FF, #E1F0FF);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--text-secondary, #666);
  font-size: 12px;
  border-radius: 8px;
  border: 1px solid rgba(74, 144, 226, 0.2);
}

.prompt-text {
  max-width: 180px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: var(--text-primary, #333);
}

.text-gray {
  color: var(--text-secondary, #666);
}

/* 按钮样式增强 */
:deep(.ant-btn) {
  border-radius: 8px !important;
  transition: all 0.3s ease !important;
}

:deep(.ant-btn:hover) {
  transform: translateY(-1px) !important;
}

:deep(.ant-btn-primary) {
  background: linear-gradient(135deg, #4A90E2, #1E5CA9);
  border: none;
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.3s ease;
}

:deep(.ant-btn-primary:hover) {
  background: linear-gradient(135deg, #1E5CA9, #0A3D62);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(74, 144, 226, 0.3);
}

:deep(.ant-btn-dangerous) {
  border-color: rgba(255, 77, 79, 0.3) !important;
  border-radius: 6px;
  transition: all 0.3s ease;
}

:deep(.ant-btn-dangerous:hover) {
  box-shadow: 0 4px 12px rgba(255, 77, 79, 0.3) !important;
  transform: translateY(-1px);
}

/* 分页样式 */
:deep(.ant-pagination) {
  margin-top: 24px !important;
  text-align: center !important;
}

:deep(.ant-pagination-item) {
  border-radius: 8px !important;
  border-color: rgba(74, 144, 226, 0.3) !important;
  transition: all 0.3s ease !important;
}

:deep(.ant-pagination-item:hover) {
  border-color: #4A90E2 !important;
  transform: translateY(-1px) !important;
}

:deep(.ant-pagination-item-active) {
  background: linear-gradient(135deg, #4A90E2, #7EB3F5) !important;
    border-color: #4A90E2 !important;
}

:deep(.ant-pagination-item-active a) {
  color: white !important;
}

/* 排序按钮样式 - 与管理员版本一致 */
:deep(.sort-buttons) {
  display: flex;
  flex-direction: column;
  margin-left: 8px;
}

:deep(.sort-button) {
  border: none;
  background: none;
  cursor: pointer;
  color: #4A90E2;
  font-size: 10px;
  line-height: 1;
  transition: all 0.2s ease;
  padding: 1px 2px;
}

:deep(.sort-button:hover) {
  color: #1E5CA9;
  transform: scale(1.2);
}

/* 应用名称链接样式 */
.app-name-link {
  font-weight: 600;
  color: #4A90E2 !important;
  padding: 0 !important;
  text-decoration: none;
  border: none;
  background: none;
  box-shadow: none !important;
}

.app-name-link:hover {
  color: #1E5CA9 !important;
  text-decoration: underline;
  background: none !important;
  border: none !important;
  box-shadow: none !important;
}

.app-name-link:focus {
  color: #1E5CA9 !important;
  background: none !important;
  border: none !important;
  box-shadow: none !important;
}

/* 响应式设计 */
@media (max-width: 768px) {
  :deep(.ant-form-inline) {
    padding: 16px;
  }
  
  :deep(.ant-table-wrapper) {
    padding: 16px;
  }
  
  .page-header {
    padding: 20px 16px;
  }
  
  .page-header h2 {
    font-size: 24px;
  }
}

/* 光效动画 */
@keyframes lightPulse {
  0% {
    opacity: 0.5;
  }
  100% {
    opacity: 0.9;
  }
}
</style>
