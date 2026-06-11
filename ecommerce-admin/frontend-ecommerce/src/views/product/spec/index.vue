<template>
  <div class="spec-container">
    <el-card class="search-card">
      <el-form :inline="true" :model="queryParams">
        <el-form-item label="所属分类">
          <el-cascader v-model="selectedCategory" :options="categoryOptions" :props="{ checkStrictly: true, value: 'id', label: 'name', children: 'children' }" placeholder="请选择分类" clearable @change="handleCategoryChange" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-row :gutter="20">
      <!-- 规格列表 -->
      <el-col :span="10">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>规格列表</span>
              <el-button type="primary" @click="handleAddSpec">新增规格</el-button>
            </div>
          </template>
          <el-table :data="specList" v-loading="loading" border stripe highlight-current-row @current-change="handleSpecSelect">
            <el-table-column prop="id" label="ID" width="60" />
            <el-table-column prop="name" label="规格名称" />
            <el-table-column prop="sort" label="排序" width="60" />
            <el-table-column label="操作" width="120">
              <template #default="{ row }">
                <el-button type="primary" link @click="handleEditSpec(row)">编辑</el-button>
                <el-button type="danger" link @click="handleDeleteSpec(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <!-- 规格值列表 -->
      <el-col :span="14">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>规格值列表 - {{ currentSpec ? currentSpec.name : '请选择规格' }}</span>
              <el-button type="primary" :disabled="!currentSpec" @click="handleAddSpecValue">新增规格值</el-button>
            </div>
          </template>
          <el-table :data="specValueList" v-loading="valueLoading" border stripe>
            <el-table-column prop="id" label="ID" width="60" />
            <el-table-column prop="value" label="规格值" />
            <el-table-column prop="sort" label="排序" width="60" />
            <el-table-column label="操作" width="80">
              <template #default="{ row }">
                <el-button type="danger" link @click="handleDeleteSpecValue(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- 新增/编辑规格对话框 -->
    <el-dialog v-model="specDialogVisible" :title="specDialogTitle" width="400px">
      <el-form ref="specFormRef" :model="specForm" :rules="specRules" label-width="80px">
        <el-form-item label="所属分类" prop="categoryId">
          <el-cascader v-model="specForm.categoryId" :options="categoryOptions" :props="{ checkStrictly: true, value: 'id', label: 'name', children: 'children' }" placeholder="请选择分类" />
        </el-form-item>
        <el-form-item label="规格名称" prop="name">
          <el-input v-model="specForm.name" placeholder="请输入规格名称" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="specForm.sort" :min="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="specDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSpecSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 新增规格值对话框 -->
    <el-dialog v-model="valueDialogVisible" title="新增规格值" width="400px">
      <el-form ref="valueFormRef" :model="valueForm" :rules="valueRules" label-width="80px">
        <el-form-item label="规格值" prop="value">
          <el-input v-model="valueForm.value" placeholder="请输入规格值" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="valueForm.sort" :min="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="valueDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleValueSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listSpecByCategory, listSpecValues, addSpec, updateSpec, deleteSpec, addSpecValue, deleteSpecValue, listAllSpecs } from '@/api/spec'
import { getCategoryTree } from '@/api/product'

const loading = ref(false)
const valueLoading = ref(false)
const specList = ref([])
const specValueList = ref([])
const categoryOptions = ref([])
const selectedCategory = ref(null)
const currentSpec = ref(null)

const specDialogVisible = ref(false)
const specDialogTitle = ref('')
const specFormRef = ref(null)

const valueDialogVisible = ref(false)
const valueFormRef = ref(null)

const specForm = reactive({
  id: null,
  name: '',
  categoryId: null,
  sort: 0
})

const valueForm = reactive({
  specId: null,
  value: '',
  sort: 0
})

const specRules = {
  name: [{ required: true, message: '请输入规格名称', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }]
}

const valueRules = {
  value: [{ required: true, message: '请输入规格值', trigger: 'blur' }]
}

const fetchCategories = async () => {
  const res = await getCategoryTree()
  categoryOptions.value = res.data
}

// 加载所有规格
const fetchAllSpecs = async () => {
  loading.value = true
  try {
    const res = await listAllSpecs()
    specList.value = res.data || []
  } catch (error) {
    console.error(error)
    specList.value = []
  } finally {
    loading.value = false
  }
}

// 根据分类筛选规格
const fetchSpecList = async () => {
  if (!selectedCategory.value || selectedCategory.value.length === 0) {
    // 没有选择分类时加载所有规格
    await fetchAllSpecs()
    return
  }
  loading.value = true
  try {
    const categoryId = selectedCategory.value[selectedCategory.value.length - 1]
    const res = await listSpecByCategory(categoryId)
    specList.value = res.data || []
  } catch (error) {
    console.error(error)
    specList.value = []
  } finally {
    loading.value = false
  }
}

const fetchSpecValues = async () => {
  if (!currentSpec.value) {
    specValueList.value = []
    return
  }
  valueLoading.value = true
  try {
    const res = await listSpecValues(currentSpec.value.id)
    specValueList.value = res.data
  } finally {
    valueLoading.value = false
  }
}

const handleCategoryChange = () => {
  currentSpec.value = null
  fetchSpecList()
}

const handleSearch = () => {
  fetchSpecList()
}

const resetQuery = () => {
  selectedCategory.value = null
  currentSpec.value = null
  specList.value = []
  specValueList.value = []
}

const handleSpecSelect = (row) => {
  currentSpec.value = row
  fetchSpecValues()
}

const handleAddSpec = () => {
  specDialogTitle.value = '新增规格'
  Object.assign(specForm, { id: null, name: '', categoryId: selectedCategory.value?.[selectedCategory.value.length - 1] || null, sort: 0 })
  specDialogVisible.value = true
}

const handleEditSpec = (row) => {
  specDialogTitle.value = '编辑规格'
  Object.assign(specForm, row)
  specDialogVisible.value = true
}

const handleSpecSubmit = async () => {
  await specFormRef.value.validate()
  if (specForm.id) {
    await updateSpec(specForm)
    ElMessage.success('修改成功')
  } else {
    await addSpec(specForm)
    ElMessage.success('新增成功')
  }
  specDialogVisible.value = false
  fetchSpecList()
}

const handleDeleteSpec = (row) => {
  ElMessageBox.confirm('确认删除该规格吗？', '提示', { type: 'warning' }).then(async () => {
    await deleteSpec(row.id)
    ElMessage.success('删除成功')
    currentSpec.value = null
    fetchSpecList()
    specValueList.value = []
  })
}

const handleAddSpecValue = () => {
  Object.assign(valueForm, { specId: currentSpec.value.id, value: '', sort: 0 })
  valueDialogVisible.value = true
}

const handleValueSubmit = async () => {
  await valueFormRef.value.validate()
  await addSpecValue(valueForm)
  ElMessage.success('新增成功')
  valueDialogVisible.value = false
  fetchSpecValues()
}

const handleDeleteSpecValue = (row) => {
  ElMessageBox.confirm('确认删除该规格值吗？', '提示', { type: 'warning' }).then(async () => {
    await deleteSpecValue(row.id)
    ElMessage.success('删除成功')
    fetchSpecValues()
  })
}

onMounted(() => {
  fetchCategories()
  fetchAllSpecs()
})
</script>

<style scoped>
.spec-container {
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
</style>
