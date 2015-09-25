package com.soagrowers.todo.service;

import com.soagrowers.todo.TodoQueryApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/todo")
public class TodoController {

    private static final Logger LOG = LoggerFactory.getLogger(TodoController.class);
    private static final TodoQueryApi api = TodoQueryApi.getInstance();

    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    String getTodo(
            @RequestParam(value = "id", required = false) String id
    ) {

        LOG.debug("GET: id={}", id);
        return "Not yet implemented";
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public @ResponseBody String detTodoCount() {

        LOG.debug("COUNT: todo={} done={}",api.getTodoCount(), api.getDoneCount() );
        return String.format("COUNT: todo=%s done=%s",api.getTodoCount(), api.getDoneCount());
    }

}
