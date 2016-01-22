package com.soagrowers.product;

import com.soagrowers.product.commands.AddProductCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by ben on 19/01/16.
 */
@RestController
public class ProductRestController {

    private static final Logger LOG = LoggerFactory.getLogger(ProductRestController.class);

    @RequestMapping(value = "/product/add/{id}", method = RequestMethod.POST)
    public void addProduct(@PathVariable(value = "id") String id,
                           @RequestParam(value = "name", required = true, defaultValue = "Un-named Product!") String name,
                           HttpServletResponse response) {

        LOG.info("ADD PRODUCT request received: [{}] '{}'", id, name);
        AddProductCommand command = new AddProductCommand(id, name);
        ProductCommandApi.getGateway().send(command);
        LOG.info("AddProductCommand sent to command gateway for processing: [{}] '{}'", id, name);

        // Set up the 200 OK response
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
