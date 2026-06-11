<template>
  <div class="logistics-container">
    <el-card class="search-card">
      <el-form :inline="true" :model="queryParams">
        <el-form-item label="快递单号">
          <el-input v-model="queryParams.keyword" placeholder="请输入快递单号" clearable @keyup.enter="handleSearch" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>物流列表</span>
          <el-button type="primary" @click="handleAdd">新增物流</el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" border stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="orderNo" label="订单号" />
        <el-table-column prop="company" label="快递公司" width="120" />
        <el-table-column prop="trackingNo" label="快递单号" width="180" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleViewTrace(row)">查看轨迹</el-button>
            <el-button type="warning" link @click="handleAddTrace(row)">添加轨迹</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination class="pagination" v-model:current-page="queryParams.pageNum"
        v-model:page-size="queryParams.pageSize" :page-sizes="[10, 20, 50]" :total="total"
        layout="total, sizes, prev, pager, next, jumper" @size-change="fetchData" @current-change="fetchData" />
    </el-card>

    <!-- 新增物流对话框 -->
    <el-dialog v-model="dialogVisible" title="新增物流" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="订单ID" prop="orderId">
          <el-input-number v-model="form.orderId" :min="1" />
        </el-form-item>
        <el-form-item label="快递公司" prop="company">
          <el-input v-model="form.company" placeholder="请输入快递公司" />
        </el-form-item>
        <el-form-item label="快递单号" prop="trackingNo">
          <el-input v-model="form.trackingNo" placeholder="请输入快递单号" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 物流轨迹对话框 -->
    <el-dialog v-model="traceDialogVisible" title="物流轨迹" width="600px">
      <el-timeline>
        <el-timeline-item v-for="item in traces" :key="item.id" :timestamp="item.time" placement="top">
          {{ item.context }}
        </el-timeline-item>
      </el-timeline>
      <el-empty v-if="traces.length === 0" description="暂无轨迹信息" />
    </el-dialog>

    <!-- 添加轨迹对话框 -->
    <el-dialog v-model="addTraceDialogVisible" title="添加轨迹" width="500px">
      <el-form ref="traceFormRef" :model="traceForm" :rules="traceRules" label-width="80px">
        <el-form-item label="物流详情" prop="context">
          <el-input v-model="traceForm.context" type="textarea" :rows="3" placeholder="请输入物流详情" />
        </el-form-item>
        <el-form-item label="时间" prop="time">
          <el-date-picker v-model="traceForm.time" type="datetime" placeholder="请选择时间" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addTraceDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleTraceSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { listLogistics, addLogistics, listLogisticsTraces, addLogisticsTrace } from '@/api/logistics'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const traceDialogVisible = ref(false)
const addTraceDialogVisible = ref(false)
const traces = ref([])
const formRef = ref(null)
const traceFormRef = ref(null)

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  keyword: ''
})

const form = reactive({
  orderId: null,
  company: '',
  trackingNo: ''
})

const traceForm = reactive({
  logisticsId: null,
  context: '',
  time: null
})

const rules = {
  orderId: [{ required: true, message: '请输入订单ID', trigger: 'blur' }],
  company: [{ required: true, message: '请输入快递公司', trigger: 'blur' }],
  trackingNo: [{ required: true, message: '请输入快递单号', trigger: 'blur' }]
}

const traceRules = {
  context: [{ required: true, message: '请输入物流详情', trigger: 'blur' }],
  time: [{ required: true, message: '请选择时间', trigger: 'change' }]
}

const getStatusType = (status) => {
  const types = { 0: 'info', 1: 'warning', 2: 'primary', 3: 'success' }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = { 0: '待发货', 1: '已发货', 2: '运输中', 3: '已签收' }
  return texts[status] || '未知'
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await listLogistics(queryParams)
    tableData.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  queryParams.pageNum = 1
  fetchData()
}

const resetQuery = () => {
  queryParams.keyword = ''
  handleSearch()
}

const handleAdd = () => {
  Object.assign(form, { orderId: null, company: '', trackingNo: '' })
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate()
  await addLogistics(form)
  ElMessage.success('新增成功')
  dialogVisible.value = false
  fetchData()
}

const handleViewTrace = async (row) => {
  const res = await listLogisticsTraces(row.id)
  traces.value = res.data
  traceDialogVisible.value = true
}

const handleAddTrace = (row) => {
  Object.assign(traceForm, { logisticsId: row.id, context: '', time: null })
  addTraceDialogVisible.value = true
}

const handleTraceSubmit = async () => {
  await traceFormRef.value.validate()
  await addLogisticsTrace(traceForm)
  ElMessage.success('添加成功')
  addTraceDialogVisible.value = false
}

onMounted(fetchData)
</script>

<style scoped>
.logistics-container {
  padding: 20px;
}
.search-card {
  margin-bottom: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.pagination {
  margin-top: 20px;
  justify-content: flex-end;
}
</style>
