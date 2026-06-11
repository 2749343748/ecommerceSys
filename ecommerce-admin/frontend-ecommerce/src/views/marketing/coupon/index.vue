<template>
  <div class="coupon-container">
    <el-card class="search-card">
      <el-form :inline="true" :model="queryParams">
        <el-form-item label="优惠券名称">
          <el-input v-model="queryParams.keyword" placeholder="请输入优惠券名称" clearable @keyup.enter="handleSearch" />
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
          <span>优惠券列表</span>
          <el-button type="primary" @click="handleAdd">新增优惠券</el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" border stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="优惠券名称" />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="row.type === 1 ? 'primary' : row.type === 2 ? 'success' : 'warning'">
              {{ row.type === 1 ? '满减' : row.type === 2 ? '折扣' : '无门槛' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="value" label="优惠值" width="100" />
        <el-table-column prop="minAmount" label="最低消费" width="100" />
        <el-table-column label="使用情况" width="120">
          <template #default="{ row }">
            {{ row.used }} / {{ row.total }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-switch v-model="row.status" :active-value="1" :inactive-value="0"
              @change="handleStatusChange(row)" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination class="pagination" v-model:current-page="queryParams.pageNum"
        v-model:page-size="queryParams.pageSize" :page-sizes="[10, 20, 50]" :total="total"
        layout="total, sizes, prev, pager, next, jumper" @size-change="fetchData" @current-change="fetchData" />
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="优惠券名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入优惠券名称" />
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择类型">
            <el-option label="满减" :value="1" />
            <el-option label="折扣" :value="2" />
            <el-option label="无门槛" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="优惠值" prop="value">
          <el-input-number v-model="form.value" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="最低消费">
          <el-input-number v-model="form.minAmount" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="发行总量" prop="total">
          <el-input-number v-model="form.total" :min="1" />
        </el-form-item>
        <el-form-item label="有效期" prop="startTime">
          <el-date-picker v-model="form.dateRange" type="datetimerange" start-placeholder="开始时间"
            end-placeholder="结束时间" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listCoupon, addCoupon, updateCoupon, deleteCoupon, updateCouponStatus } from '@/api/coupon'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  keyword: ''
})

const form = reactive({
  id: null,
  name: '',
  type: 1,
  value: 0,
  minAmount: 0,
  total: 100,
  dateRange: null
})

const rules = {
  name: [{ required: true, message: '请输入优惠券名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择类型', trigger: 'change' }],
  value: [{ required: true, message: '请输入优惠值', trigger: 'blur' }],
  total: [{ required: true, message: '请输入发行总量', trigger: 'blur' }]
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await listCoupon(queryParams)
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
  dialogTitle.value = '新增优惠券'
  Object.assign(form, { id: null, name: '', type: 1, value: 0, minAmount: 0, total: 100, dateRange: null })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑优惠券'
  Object.assign(form, { ...row, dateRange: [row.startTime, row.endTime] })
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate()
  if (form.dateRange) {
    form.startTime = form.dateRange[0]
    form.endTime = form.dateRange[1]
  }
  if (form.id) {
    await updateCoupon(form)
    ElMessage.success('修改成功')
  } else {
    await addCoupon(form)
    ElMessage.success('新增成功')
  }
  dialogVisible.value = false
  fetchData()
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确认删除该优惠券吗？', '提示', { type: 'warning' }).then(async () => {
    await deleteCoupon(row.id)
    ElMessage.success('删除成功')
    fetchData()
  })
}

const handleStatusChange = async (row) => {
  await updateCouponStatus(row.id, row.status)
  ElMessage.success('状态修改成功')
}

onMounted(fetchData)
</script>

<style scoped>
.coupon-container {
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
