package cn.iocoder.yudao.module.infra.controller.admin.demo02;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.module.infra.controller.admin.demo02.vo.*;
import cn.iocoder.yudao.module.infra.convert.demo02.InfraDemoStudentConvert;
import cn.iocoder.yudao.module.infra.dal.dataobject.demo02.InfraDemoStudentDO;
import cn.iocoder.yudao.module.infra.service.demo02.InfraDemoStudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;

@Tag(name = "管理后台 - 学生")
@RestController
@RequestMapping("/infra/demo-student")
@Validated
public class InfraDemoStudentController {

    @Resource
    private InfraDemoStudentService demoStudentService;

    @PostMapping("/create")
    @Operation(summary = "创建学生")
    @PreAuthorize("@ss.hasPermission('infra:demo-student:create')")
    public CommonResult<Long> createDemoStudent(@Valid @RequestBody InfraDemoStudentCreateReqVO createReqVO) {
        return success(demoStudentService.createDemoStudent(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新学生")
    @PreAuthorize("@ss.hasPermission('infra:demo-student:update')")
    public CommonResult<Boolean> updateDemoStudent(@Valid @RequestBody InfraDemoStudentUpdateReqVO updateReqVO) {
        demoStudentService.updateDemoStudent(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除学生")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('infra:demo-student:delete')")
    public CommonResult<Boolean> deleteDemoStudent(@RequestParam("id") Long id) {
        demoStudentService.deleteDemoStudent(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得学生")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('infra:demo-student:query')")
    public CommonResult<InfraDemoStudentRespVO> getDemoStudent(@RequestParam("id") Long id) {
        InfraDemoStudentDO demoStudent = demoStudentService.getDemoStudent(id);
        return success(InfraDemoStudentConvert.INSTANCE.convert(demoStudent));
    }

    @GetMapping("/page")
    @Operation(summary = "获得学生分页")
    @PreAuthorize("@ss.hasPermission('infra:demo-student:query')")
    public CommonResult<PageResult<InfraDemoStudentRespVO>> getDemoStudentPage(@Valid InfraDemoStudentPageReqVO pageVO) {
        PageResult<InfraDemoStudentDO> pageResult = demoStudentService.getDemoStudentPage(pageVO);
        return success(InfraDemoStudentConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出学生 Excel")
    @PreAuthorize("@ss.hasPermission('infra:demo-student:export')")
    @OperateLog(type = EXPORT)
    public void exportDemoStudentExcel(@Valid InfraDemoStudentExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<InfraDemoStudentDO> list = demoStudentService.getDemoStudentList(exportReqVO);
        // 导出 Excel
        List<InfraDemoStudentExcelVO> datas = InfraDemoStudentConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "学生.xls", "数据", InfraDemoStudentExcelVO.class, datas);
    }

}