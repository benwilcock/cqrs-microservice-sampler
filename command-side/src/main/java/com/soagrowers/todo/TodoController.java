package com.soagrowers.todo;

import com.soagrowers.todo.commands.CreateTodoCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by ben on 19/01/16.
 */
@RestController
public class TodoController {

    private static final Logger LOG = LoggerFactory.getLogger(TodoController.class);


    @RequestMapping(value = "/create/{id}", method = RequestMethod.POST)
    public void create(@PathVariable(value = "id") String id,
                       @RequestParam(value = "desc", required = true, defaultValue = "Blank Todo") String description,
                       HttpServletResponse response) {
        LOG.info("CREATE Request received: [{}] '{}'", id, description);
        CreateTodoCommand command = new CreateTodoCommand(id, description);
        TodoCommandApi.getGateway().send(command);
        LOG.info("TodoCreated command SENT: [{}] '{}'", id, description);
        response.setStatus(HttpServletResponse.SC_OK);

    }
}
