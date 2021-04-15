package com.atguigu.aclservice.controller.activiti;

import com.atguigu.aclservice.mapper.activiti.ActivitiMapper;
import com.atguigu.utils.utils.R;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.zip.ZipInputStream;


@RestController
@RequestMapping("/admin/acl/processDefinition")
public class ProcessDefinitionController {

    @Autowired
    private RepositoryService repositoryService;

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
     * @param deploymentFileUUID
     * @return
     */
    @PostMapping(value = "/addDeploymentByFileNameBPMN")
    public R addDeploymentByFileNameBPMN(@RequestParam("deploymentFileUUID") String deploymentFileUUID, @RequestParam("deploymentName") String deploymentName) {
        try {
            String filename = "resources/bpmn/" + deploymentFileUUID;
            Deployment deployment = repositoryService.createDeployment()//初始化流程
                    .addClasspathResource(filename)
                    .name(deploymentName)
                    .deploy();
            //System.out.println(deployment.getName());
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
            Deployment deployment = repositoryService.createDeployment()
                    .addString("CreateWithBPMNJS.bpmn", map.get("bpmnStr").toString())
                    .name("在线部署名称")
                    .deploy();
            //System.out.println(deployment.getName());
            return R.ok().data(R.DESC, deployment.getId());
        } catch (Exception e) {
            return R.error().message("string部署流程失败").data(R.DESC, e.toString());
        }
    }

//缺失流程部署ID属性版本，import org.activiti.api.process.model.ProcessDefinition;
    /*@GetMapping(value = "/getDefinitions")
    public AjaxResponse getDefinitions() {

        try {
            if (GlobalConfig.Test) {
                securityUtil.logInAs("wukong");
            }
            Page<ProcessDefinition> processDefinitions = processRuntime.processDefinitions(Pageable.of(0, 50));
            System.out.println("流程定义数量： " + processDefinitions.getTotalItems());
            for (ProcessDefinition pd : processDefinitions.getContent()) {
                System.out.println("getId：" + pd.getId());
                System.out.println("getName：" + pd.getName());
                System.out.println("getStatus：" + pd.getKey());
                System.out.println("getStatus：" + pd.getFormKey());
            }


            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), processDefinitions.getContent());
        }catch (Exception e) {
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "获取流程定义失败", e.toString());
        }
    }*/


    //import org.activiti.engine.RepositoryService;
    @GetMapping(value = "/getDefinitions")
    public R getDefinitions() {

        try {
            List<HashMap<String, Object>> listMap= new ArrayList<HashMap<String, Object>>();
            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();

            list.sort((y,x)->x.getVersion()-y.getVersion());

            for (ProcessDefinition pd : list) {
                HashMap<String, Object> hashMap = new HashMap<>();
                //System.out.println("流程定义ID："+pd.getId());
                hashMap.put("processDefinitionID", pd.getId());
                hashMap.put("name", pd.getName());
                hashMap.put("key", pd.getKey());
                hashMap.put("resourceName", pd.getResourceName());
                hashMap.put("deploymentID", pd.getDeploymentId());
                hashMap.put("version", pd.getVersion());
                listMap.add(hashMap);
            }


            return R.ok().data(R.ITEMS, listMap);
        }catch (Exception e) {
            return R.error().message("获取流程定义失败").data(R.DESC, e.toString());
        }
    }

    //获取流程定义XML
    @GetMapping(value = "/getDefinitionXML")
    public void getProcessDefineXML(HttpServletResponse response,
                                    @RequestParam("deploymentId") String deploymentId,
                                    @RequestParam("resourceName") String resourceName) {
        try {
            InputStream inputStream = repositoryService.getResourceAsStream(deploymentId,resourceName);
            int count = inputStream.available();
            byte[] bytes = new byte[count];
            response.setContentType("text/xml");
            OutputStream outputStream = response.getOutputStream();
            while (inputStream.read(bytes) != -1) {
                outputStream.write(bytes);
            }
            inputStream.close();
        } catch (Exception e) {
            e.toString();
        }
    }

    /**
     * 获取所有部署流程
     * @return
     */
    @GetMapping(value = "/getDeployments")
    public R getDeployments() {
        try {

            List<HashMap<String, Object>> listMap= new ArrayList<HashMap<String, Object>>();
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



    //删除流程定义
    @GetMapping(value = "/delDefinition")
    public R delDefinition(@RequestParam("depID") String depID, @RequestParam("pdID") String pdID) {
        try {

            //删除数据
            int result = mapper.DeleteFormData(pdID);

            repositoryService.deleteDeployment(depID, true);

            return R.ok().message("删除成功");
        } catch (Exception e) {
            return R.error().message("删除失败").data(R.DESC, e.toString());
        }
    }

}
