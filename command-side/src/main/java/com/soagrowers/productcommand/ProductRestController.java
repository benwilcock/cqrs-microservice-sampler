package com.soagrowers.productcommand;

import com.soagrowers.productcommand.commands.AddProductCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by ben on 19/01/16.
 */
@RestController
public class ProductRestController {

    private static final Logger LOG = LoggerFactory.getLogger(ProductRestController.class);

    @Autowired
    CommandGateway commandGateway;

    @RequestMapping(value = "/products/add/{id}", method = RequestMethod.POST)
    public void addProduct(@PathVariable(value = "id") String id,
                           @RequestParam(value = "name", required = true, defaultValue = "Un-named Product!") String name,
                           HttpServletResponse response) {

        LOG.info("ADD PRODUCT API request received: [{}] '{}'", id, name);

        AddProductCommand command = new AddProductCommand(id, name);
        commandGateway.sendAndWait(command);
        LOG.info("ADD PRODUCT COMMAND sent to GATEWAY: Product [{}] '{}'", id, name);

        // Set up the 200 OK response
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
