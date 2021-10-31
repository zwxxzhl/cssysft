package com.atguigu.aclservice.controller.sys;


import com.atguigu.aclservice.entity.sys.Dict;
import com.atguigu.aclservice.service.sys.IDictService;
import com.atguigu.aclservice.util.BaseController;
import com.atguigu.utils.utils.R;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * 字典类别表 前端控制器
 * </p>
 *
 * @author zwx
 * @since 2021-10-25
 */
@RestController
@RequestMapping("/admin/acl/dict")
public class DictController extends BaseController<Dict, IDictService> {

    public DictController() {
        super.PK = "id";
        super.UNI_VALID = true;
        super.UNI_COLUMN = "code";
        super.WRAPPER_ADD_DELETE = false;
        super.DELETE_COLUMN = "is_deleted";
    }

    @GetMapping("/ceshi")
    public R select(HttpServletRequest request, @RequestParam Map<String, Object> params) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> list = new ArrayList<>();
            params.forEach((k, v) -> {
                Map<String, Object> km = null;
                try {
                    km = objectMapper.readValue(params.get(k).toString(), new TypeReference<Map<String,Object>>(){});
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                if (Optional.ofNullable(km).isPresent()) {
                    list.add(km);
                }
            });


            JsonParser jsonParser = new JsonFactory().createParser(params.get("jsonParams").toString());
            Map<String, Object> paramsMap = objectMapper.readValue(jsonParser, new TypeReference<Map<String, Object>>() {});

            Map<String, Object> jsonParams = objectMapper.readValue(params.get("jsonParams").toString(), new TypeReference<Map<String,Object>>(){});

            System.out.println(paramsMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.ok();
    }



}

