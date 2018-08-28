package com.pankesh.productcommand;

import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;

import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.model.ConcurrencyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pankesh.productcommand.commands.AddProductCommand;
import com.pankesh.productcommand.commands.MarkProductAsSaleableCommand;
import com.pankesh.productcommand.commands.MarkProductAsUnsaleableCommand;
import com.pankesh.utils.Asserts;

/**
 * Created by ben on 19/01/16.
 */
@RestController
@RequestMapping("/products")
public class ProductRestController {

    private static final Logger LOG = LoggerFactory.getLogger(ProductRestController.class);

    @Autowired
    CommandGateway commandGateway;

    @RequestMapping(value = "/add/{id}", method = RequestMethod.POST)
    public void add(@PathVariable(value = "id") String id, @RequestParam(value = "name", required = true) String name,
            HttpServletResponse response) {

        LOG.debug("Adding Product [{}] '{}'", id, name);

        try {
            Asserts.INSTANCE.areNotEmpty(Arrays.asList(id, name));
            AddProductCommand command = new AddProductCommand(id, name);
            commandGateway.sendAndWait(command);
            LOG.info("Added Product [{}] '{}'", id, name);
            response.setStatus(HttpServletResponse.SC_CREATED);// Set up the 201
                                                               // CREATED
                                                               // response
        } catch (AssertionError ae) {
            LOG.warn("Add Request failed - empty params?. [{}] '{}'", id, name);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (ConcurrencyException concEx) {
            LOG.warn("A duplicate product with the same ID [{}] already exists.", id);
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        } catch (CommandExecutionException cex) {
            LOG.warn("Add Command FAILED with Message: {}", cex.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        }
    }

    @RequestMapping(value = "{id}/saleable/{saleableFlag}", method = RequestMethod.PUT)
    public void makeSaleable(@PathVariable(value = "id") String id, @PathVariable(value = "saleableFlag", required = true) boolean saleableFlag,
            HttpServletResponse response) {

        LOG.debug("Changing Product [{}] '{}'", id, saleableFlag);

        try {
            if (saleableFlag) {
                MarkProductAsSaleableCommand productSaleableCommand = new MarkProductAsSaleableCommand(id);
                commandGateway.sendAndWait(productSaleableCommand);
            } else {
                MarkProductAsUnsaleableCommand productUnsaleableCommand = new MarkProductAsUnsaleableCommand(id);                
                commandGateway.sendAndWait(productUnsaleableCommand);
            }
            LOG.info("Changed Product [{}] '{}'", id, saleableFlag);
            response.setStatus(HttpServletResponse.SC_ACCEPTED);// Set up the 202 ACCEPTED response
        } catch (AssertionError ae) {
            LOG.warn("Change Request failed - empty params?. [{}] '{}'", id, saleableFlag, ae);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (CommandExecutionException cex) {
            LOG.warn("Change Command FAILED with Message: {}", cex.getMessage(), cex);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

}
