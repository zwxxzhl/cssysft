package com.atguigu.aclservice.controller.activiti;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.aclservice.mapper.activiti.ActivitiMapper;
import com.atguigu.utils.utils.R;
import io.swagger.annotations.ApiParam;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.core.common.project.model.ProjectManifest;
import org.activiti.core.common.spring.project.ProjectModelService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipInputStream;


@RestController
@RequestMapping("/admin/acl/processDefinition")
public class ProcessDefinitionController {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ProjectModelService projectModelService;

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    ActivitiMapper mapper;

    @Value("${bpmn-path-mapping}")
    private String bpmnPathMapping;


    @PostMapping(value = "/uploadStreamAndDeployment")
    public R uploadStreamAndDeployment(@RequestParam("processFile") MultipartFile multipartFile) {
        // 获取上传的文件名
        String fileName = multipartFile.getOriginalFilename();

        try {
            // 得到输入流（字节流）对象
            InputStream fileInputStream = multipartFile.getInputStream();

            // 文件的扩展名
            String extension = FilenameUtils.getExtension(fileName);

            // ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();//创建处理引擎实例
            // repositoryService = processEngine.getRepositoryService();//创建仓库服务实例

            Deployment deployment = null;
            if (extension.equals("zip")) {
                ZipInputStream zip = new ZipInputStream(fileInputStream);
                deployment = repositoryService.createDeployment()//初始化流程
                        .addZipInputStream(zip)
                        .name("流程部署名称可通过接口传递现在写死")
                        .deploy();
            } else {
                deployment = repositoryService.createDeployment()//初始化流程
                        .addInputStream(fileName, fileInputStream)
                        .name("流程部署名称可通过接口传递现在写死")
                        .deploy();
            }

            return R.ok().data("id", deployment.getId()).data("fileName", fileName);
        } catch (Exception e) {
            return R.error().message("部署流程失败").data(R.DESC, e.toString());
        }
    }


    @PostMapping(value = "/upload")
    public R upload(HttpServletRequest request, @RequestParam("processFile") MultipartFile multipartFile) {

        if (multipartFile.isEmpty()) {
            System.out.println("文件为空");
        }
        String fileName = multipartFile.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        String filePath = bpmnPathMapping; // 上传后的路径

        //本地路径格式转上传路径格式
        filePath = filePath.replace("\\", "/");
        filePath = filePath.replace("file:", "");

        // String filePath = request.getSession().getServletContext().getRealPath("/") + "bpmn/";
        fileName = UUID.randomUUID() + suffixName; // 新文件名
        File file = new File(filePath + fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            multipartFile.transferTo(file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return R.ok().data(R.DESC, fileName);
    }


    /**
     * 根据路径文件，部署流程
     */
    @PostMapping(value = "/addDeploymentByFileNameBpmn")
    public R addDeploymentByFileNameBpmn(@RequestParam("deploymentFileUuid") String deploymentFileUuid, @RequestParam("deploymentName") String deploymentName) {
        try {
            String filename = "resources/bpmn/" + deploymentFileUuid;
            Deployment deployment = repositoryService.createDeployment()
                    .addClasspathResource(filename)
                    .name(deploymentName)
                    .deploy();

            return R.ok().data(R.DESC, deployment.getId());
        } catch (Exception e) {
            return R.error().message("BPMN部署流程失败").data(R.DESC, e.toString());
        }

    }

    /**
     * 根据字符串部署流程，在线部署
     */
    @PostMapping(value = "/addDeploymentByString")
    public R addDeploymentByString(@RequestBody Map<String, Object> map) {
        try {
            ProjectManifest projectManifest = projectModelService.loadProjectManifest();
            Deployment deployment = repositoryService.createDeployment()
                    .setProjectManifest(projectManifest)
                    .addString("CreateWithBPMNJS.bpmn", map.get("bpmnStr").toString())
                    .name("在线部署名称")
                    .deploy();
            //System.out.println(deployment.getName());
            return R.ok().data(R.DESC, deployment.getId());
        } catch (Exception e) {
            return R.error().message("string部署流程失败").data(R.DESC, e.toString());
        }
    }

    /**
     * 查询流程定义--分页
     */
    @GetMapping(value = "/getDefinitions/{page}/{limit}")
    public R getDefinitions(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable int page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable int limit,

            @ApiParam(name = "searchObj", value = "查询对象") ProcessDefinition searchObj) {

        try {
            List<HashMap<String, Object>> listMap = new ArrayList<>();

            ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
            if (!StringUtils.isEmpty(searchObj.getName())) {
                query.processDefinitionNameLike(searchObj.getName());
            }
            query.orderByProcessDefinitionVersion().desc();

            List<ProcessDefinition> list = query.listPage((page - 1) * limit, limit);
            long count = query.count();

            for (ProcessDefinition pd : list) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id", pd.getId());
                hashMap.put("name", pd.getName());
                hashMap.put("key", pd.getKey());
                hashMap.put("resourceName", pd.getResourceName());
                hashMap.put("deploymentId", pd.getDeploymentId());
                hashMap.put("version", pd.getVersion());
                listMap.add(hashMap);
            }

            return R.ok().data("items", listMap).data("total", count);
        } catch (Exception e) {
            return R.error().message("获取流程定义列表失败").data(R.DESC, e.toString());
        }
    }

    /**
     * 获取流程定义xml
     */
    @GetMapping(value = "/getProcessDefinitionXml")
    public R getProcessDefinitionXml(@RequestParam("id") String processDefinitionId) {
        try {
            BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
            BpmnXMLConverter converter = new BpmnXMLConverter();
            String xml = new String(converter.convertToXML(bpmnModel));

            return R.ok().data("xml", xml);
        } catch (Exception e) {
            return R.error().message("获取流程定义xml失败").data(R.DESC, e.toString());
        }
    }

    /**
     * 获取所有部署流程
     */
    @GetMapping(value = "/getDeployments")
    public R getDeployments() {
        try {
            List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();
            List<Deployment> list = repositoryService.createDeploymentQuery().list();
            for (Deployment dep : list) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id", dep.getId());
                hashMap.put("name", dep.getName());
                hashMap.put("deploymentTime", dep.getDeploymentTime());
                listMap.add(hashMap);
            }

            return R.ok().data(R.ITEMS, listMap);
        } catch (Exception e) {
            return R.error().message("查询失败").data(R.DESC, e.toString());
        }
    }


    /**
     * 删除流程定义
     */
    @DeleteMapping(value = "/delDefinition")
    public R delDefinition(@RequestBody JSONObject params) {
        try {
            repositoryService.deleteDeployment(params.getString("deploymentId"), params.getBoolean("cascade"));
            return R.ok().message("删除成功");
        } catch (Exception e) {
            return R.error().message("删除失败").data(R.DESC, e.toString());
        }
    }

}
