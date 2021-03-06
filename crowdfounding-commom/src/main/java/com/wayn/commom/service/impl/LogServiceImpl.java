package com.wayn.commom.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.wayn.commom.dao.LogDao;
import com.wayn.commom.domain.OperLog;
import com.wayn.commom.enums.Operator;
import com.wayn.commom.excel.IExcelExportStylerImpl;
import com.wayn.commom.service.LogService;
import com.wayn.commom.util.FileUtils;
import com.wayn.commom.util.ParameterUtil;
import com.wayn.commom.util.UserAgentUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 日志表 服务实现类
 * </p>
 *
 * @author wayn
 * @since 2019-04-13
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogDao, OperLog> implements LogService {
    private static final Map<String, String> map;

    static {
        map = new HashMap<>();
        for (Operator operator : Operator.values()) {
            map.put(operator.getCode(), operator.getName());
        }
    }

    @Override
    public Page<OperLog> listPage(Page<OperLog> page, OperLog log) {
        EntityWrapper<OperLog> wrapper = ParameterUtil.get();
        wrapper.like("userName", log.getUserName());
        wrapper.like("moduleName", log.getModuleName());
        wrapper.like("ip", log.getIp());
        wrapper.eq(StringUtils.isNotEmpty(log.getOperation()), "operation", log.getOperation());
        wrapper.eq(log.getOperState() != null, "operState", log.getOperState());
        wrapper.eq(StringUtils.isNotEmpty(log.getOperation()), "operation", log.getOperation());
        Page<OperLog> logPage = selectPage(page, wrapper);
        for (OperLog record : logPage.getRecords()) {
            record.setOperation(map.get(record.getOperation()));
        }
        return logPage;
    }

    @Override
    public void export(OperLog log, HttpServletResponse response, HttpServletRequest request) throws IOException {
        EntityWrapper<OperLog> wrapper = ParameterUtil.get();
        wrapper.like("userName", log.getUserName());
        wrapper.like("moduleName", log.getModuleName());
        wrapper.like("ip", log.getIp());
        wrapper.eq(StringUtils.isNotEmpty(log.getOperation()), "operation", log.getOperation());
        wrapper.eq(log.getOperState() != null, "operState", log.getOperState());
        wrapper.eq(StringUtils.isNotEmpty(log.getOperation()), "operation", log.getOperation());
        List<OperLog> list = selectList(wrapper);
        ExportParams exportParams = new ExportParams();
        exportParams.setStyle(IExcelExportStylerImpl.class);
        exportParams.setColor(HSSFColor.HSSFColorPredefined.GREEN.getIndex());
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams,
                OperLog.class, list);
        // 使用bos获取excl文件大小
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Length", bos.size() + "");
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, "日志列表.xls"));
        response.setContentType("application/octet-stream;charset=UTF-8");
        //保存数据
        OutputStream os = response.getOutputStream();
        workbook.write(os);
        workbook.close();
        os.close();
        bos.close();
    }

    @Override
    public OperLog detail(String id) {
        OperLog operLog = selectById(id);
        String browserName = UserAgentUtils.getBrowserName(operLog.getAgent());
        String osName = UserAgentUtils.getOsName(operLog.getAgent());
        operLog.setAgent(browserName + "\t" + osName);
        operLog.setOperation(map.get(operLog.getOperation()));
        return operLog;
    }
}
