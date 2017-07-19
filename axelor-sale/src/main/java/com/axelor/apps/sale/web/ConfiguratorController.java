/**
 * Axelor Business Solutions
 *
 * Copyright (C) 2017 Axelor (<http://axelor.com>).
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axelor.apps.sale.web;

import com.axelor.apps.base.db.Product;
import com.axelor.apps.sale.db.Configurator;
import com.axelor.apps.sale.db.SaleOrder;
import com.axelor.apps.sale.db.repo.ConfiguratorRepository;
import com.axelor.apps.sale.db.repo.SaleOrderRepository;
import com.axelor.apps.sale.service.ConfiguratorService;
import com.axelor.exception.service.TraceBackService;
import com.axelor.i18n.I18n;
import com.axelor.inject.Beans;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.rpc.JsonContext;
import com.google.inject.Inject;

public class ConfiguratorController {

    private ConfiguratorRepository configuratorRepository;
    private ConfiguratorService configuratorService;

    @Inject
    ConfiguratorController(ConfiguratorRepository configuratorRepository,
                           ConfiguratorService configuratorService) {
        this.configuratorRepository = configuratorRepository;
        this.configuratorService = configuratorService;
    }

    /**
     * Called from configurator form view, set values
     * for the indicators JSON field.
     * call {@link ConfiguratorService#updateIndicators(Configurator, JsonContext, JsonContext)}
     * @param request
     * @param response
     */
    public void updateIndicators(ActionRequest request, ActionResponse response) {
        Configurator configurator = request.getContext().asType(Configurator.class);
        JsonContext jsonAttributes = (JsonContext) request.getContext().get("$attributes");
        JsonContext jsonIndicators = (JsonContext) request.getContext().get("$indicators");
        configurator = configuratorRepository.find(configurator.getId());
        configuratorService.updateIndicators(configurator, jsonAttributes, jsonIndicators);
        response.setValue("indicators", request.getContext().get("indicators"));
    }

    /**
     * Called from configurator form view,
     * call {@link ConfiguratorService#generateProduct(Configurator, JsonContext, JsonContext)}
     * @param request
     * @param response
     */
    public void generateProduct(ActionRequest request, ActionResponse response) {
        Configurator configurator = request.getContext().asType(Configurator.class);
        JsonContext jsonAttributes = (JsonContext) request.getContext().get("$attributes");
        JsonContext jsonIndicators = (JsonContext) request.getContext().get("$indicators");
        configurator = configuratorRepository.find(configurator.getId());
        try {
            configuratorService.generateProduct(configurator, jsonAttributes, jsonIndicators);
            response.setReload(true);
            if (configurator.getProductId() != null) {
                response.setView(ActionView
                        .define(I18n.get("Product generated"))
                        .model(Product.class.getName())
                        .add("form", "product-form")
                        .add("grid", "product-grid")
                        .context("_showRecord", configurator.getProductId())
                        .map());
            }
        } catch (Exception e) {
            TraceBackService.trace(e);
            response.setError(e.getLocalizedMessage());
        }
    }

    /**
     * Called from configurator wizard form view,
     * call {@link ConfiguratorService#addLineToSaleOrder(Configurator, SaleOrder, JsonContext, JsonContext)}
     * @param request
     * @param response
     */
    public void generateForSaleOrder(ActionRequest request,
                                      ActionResponse response) {
        Configurator configurator = request.getContext().asType(Configurator.class);
        long saleOrderId = ((Integer) request.getContext().get("_saleOrderId")).longValue();

        JsonContext jsonAttributes = (JsonContext) request.getContext().get("$attributes");
        JsonContext jsonIndicators = (JsonContext) request.getContext().get("$indicators");

        configurator = configuratorRepository.find(configurator.getId());
        SaleOrder saleOrder = Beans.get(SaleOrderRepository.class).find(saleOrderId);
        try {
            configuratorService.addLineToSaleOrder(configurator, saleOrder,
                    jsonAttributes, jsonIndicators);
        } catch (Exception e) {
            TraceBackService.trace(e);
            response.setError(e.getLocalizedMessage());
        }
    }
}