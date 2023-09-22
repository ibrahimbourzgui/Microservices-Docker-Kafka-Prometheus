package com.ibrahim.orderservice.service;

import com.ibrahim.orderservice.dto.InventoryDto;
import com.ibrahim.orderservice.dto.OrderDto;
import com.ibrahim.orderservice.dto.OrderLineItemsDto;
import com.ibrahim.orderservice.event.OrderPlacedEvent;
import com.ibrahim.orderservice.model.OrderLineItems;
import com.ibrahim.orderservice.model.OrderP;
import com.ibrahim.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private  final WebClient.Builder webClientBuilder;
    private final ApplicationEventPublisher applicationEventPublisher;
    public String createOrder(OrderDto orderDto){
        OrderP order = new OrderP();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems = orderDto.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        order.setOrderLineItemsList(orderLineItems);


        List<String> skuCodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode)
                .collect(Collectors.toList());

        //call inventory service, and place order if product is in stock
        InventoryDto[] inventoryResponseArray = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory/isInStock",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryDto[].class)
                .block();
        boolean allProductsInstock = Arrays.stream(inventoryResponseArray).allMatch(InventoryDto::isInStock);
        if(allProductsInstock){
            orderRepository.save(order);
            applicationEventPublisher.publishEvent(new OrderPlacedEvent(this, order.getOrderNumber()));
            return "Order Placed Successfully";
        }
        else {
            throw new IllegalArgumentException("Product is not in stock");
        }

    }



    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
