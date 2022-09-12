<template>
<div>

  <div style="padding: 10px 0">
    <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" class="ml-5" v-model="name"></el-input>
    <!-- <el-input style="width: 200px" placeholder="请输入邮箱" suffix-icon="el-icon-message" class="ml-5" v-model="email"></el-input>
    <el-input style="width: 200px" placeholder="请输入地址" suffix-icon="el-icon-location" class="ml-5" v-model="address"></el-input> -->
    <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
    <el-button type="warning" @click="reset">重置</el-button>
  </div>

  <div style="margin: 10px 0">
    <el-button type="primary" @click="handleAdd">新增<i class="el-icon-circle-plus-outline"></i> </el-button>
    <el-popconfirm
        class="ml-5"
        confirm-button-text='确定'
        cancel-button-text='再想想吧'
        icon="el-icon-info"
        icon-color="red"
        title="您确定删除吗？"
        @confirm="delBatch"
    >
      <el-button type="danger" slot="reference">批量删除<i class="el-icon-remove-outline"></i></el-button>
    </el-popconfirm>
    <!--
    <el-upload action="http://localhost:9090/role/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
    <el-button type="primary" class="ml-5">导入<i class="el-icon-bottom"></i></el-button>
    </el-upload>
    <el-button type="primary" class="ml-5" @click="exp">导出<i class="el-icon-top"></i> </el-button>
    -->
  </div>

  <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="55"></el-table-column>
    <el-table-column prop="id" label="ID" width="80"></el-table-column>
    <el-table-column prop="name" label="名称"></el-table-column>
    <el-table-column prop="flag" label="唯一标识"></el-table-column>
    <el-table-column prop="description" label="描述"></el-table-column>
    <el-table-column label="操作" width="280" align="center">
      <template slot-scope="scope">
        <el-button type="info" @click="selectMenu(scope.row)" slot="reference">分配菜单<i class="el-icon-menu"></i> </el-button>
        <el-button type="warning" @click="handleUpdate(scope.row)">编辑<i class="el-icon-edit"></i></el-button>
        <el-popconfirm
            class="ml-5"
            confirm-button-text='确定'
            cancel-button-text='再想想吧'
            icon="el-icon-info"
            icon-color="red"
            title="您确定删除吗？"
            @confirm="handleDelete(scope.row.id)"
        >
          <el-button type="danger" slot="reference">删除 <i class="el-icon-delete"></i> </el-button>
        </el-popconfirm>
      </template>
    </el-table-column>
  </el-table>
  <div style="padding: 10px 0">
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNo"
        :page-sizes="[2, 3, 4, 5, 10, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>
  </div>

  <el-dialog title="角色信息" :visible.sync="dialogFormVisible" width="40%">
    <el-form label-width="80px" size="small">
      <el-form-item label="名称">
        <el-input v-model="form.name" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="描述">
        <el-input v-model="form.description" autocomplete="off"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFormVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </el-dialog>

  <el-dialog title="菜单分配" :visible.sync="menuDialogVis" width="30%">
    <el-tree
        :props="props"
        :data="menuData"
        show-checkbox
        node-key="id"
        ref="tree"
        :default-expanded-keys="expanded"
        :default-checked-keys="checked">
      <span class="custom-tree-node" slot-scope="{ node, data }">
        <span><i :class="data.icon"></i> {{ data.name }}</span>
      </span>
    </el-tree>
    <div slot="footer" class="dialog-footer">
      <el-button @click="menuDialogVis = false">取 消</el-button>
      <el-button type="primary" @click="saveRoleMenu">确 定</el-button>
    </div>
  </el-dialog>

</div>
</template>

<script>
export default {
  name: "User",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNo: 1,
      pageSize: 3,
      name: "",
      form: {},
      dialogFormVisible: false,
      menuDialogVis: false,
      multipleSelection: [],
      headerBg: 'headerBg',
      menuData: [],
      // 将菜单中的name属性映射为此处显示需要的label属性
      props: {
        label: 'name'
      },
      expended: [],
      checked: [],
      roleId: 0,
      roleFlag: ''
    }
  },

  created() {
    this.load()
  },

  methods: {
    handleSizeChange(pageSize) {
      console.log('每页 ${pageSize} 条');
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNo) {
      console.log('当前页：${pageNo} 条')
      this.pageNo = pageNo
      this.load()
    },
    load() {
      this.request.get("/role/pageSelect",{
        params: {
          pageNo: this.pageNo,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        console.log(res.data)

        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    reset() {
      this.name=""
      this.load()
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    handleUpdate(row) {
      this.form = row
      this.dialogFormVisible = true
    },
    handleDelete(id) {
      this.request.delete("/role/delete/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功!")
          this.load()
        } else {
          this.$message.error("删除失败!")
        }
      })
    },
    save() {
      // this.form是传递的参数
      this.request.post("/role/saveOrUpdate", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("用户保存成功!")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("用户保存失败!")
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      // 将一个对象的数组变为一个纯id的数组
      let ids = this.multipleSelection.map(v => v.id)
      this.request.post("/role/batchDelete", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功!")
          this.load()
        } else {
          this.$message.error("批量删除失败!")
        }
      })
    },
    selectMenu(role) {
      this.menuDialogVis = true
      this.roleId = role.id
      this.roleFlag = role.flag

      // 请求菜单数据
      this.request.get("/menu").then(res => {
        console.log(res.data)
        this.menuData = res.data

        // 将后台返回的菜单数据处理成id数组
        this.expanded = this.menuData.map(v => v.id)

      })

      this.request.get("/role/roleMenu/" + this.roleId).then(res => {
        this.checked = res.data
      })
    },
    saveRoleMenu() {
      this.request.post("/role/roleMenu/" + this.roleId, this.$refs.tree.getCheckedKeys()).then(res => {
        console.log(res);
        if (res.code === '200') {
          this.$message.success("设置成功")
          this.menuDialogVis = false

          // 如果修改的是管理员角色的菜单，那么需要重新登录；修改的是普通用户的，则不用
          if (this.roleFlag === 'ROLE_ADMIN') {
            this.$store.commit("logout")
          }
        } else {
          this.$message.error(res.message)
        }
      })
    },
  }
}
</script>

<style>
.headerBg {
  background: #eee!important;
}
</style>