package fr.insee.coleman.api.utils;

import fr.insee.coleman.api.domain.Order;
import fr.insee.coleman.api.domain.TypeManagementMonitoringInfo;
import fr.insee.coleman.api.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class StatusOrderComparator implements Comparator<TypeManagementMonitoringInfo> {

    @Autowired
    private OrderService orderService;

    @Override
    public int compare(TypeManagementMonitoringInfo o1, TypeManagementMonitoringInfo o2) {
        Order eventOrder1 = orderService.findByStatus(o1.toString());
        Order eventOrder2 = orderService.findByStatus(o2.toString());

        return Integer.compare(eventOrder2.getOrder(), eventOrder1.getOrder());
    }
}
