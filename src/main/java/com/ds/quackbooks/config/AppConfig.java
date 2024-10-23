package com.ds.quackbooks.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ds.quackbooks.models.OrderItem;
import com.ds.quackbooks.payload.OrderItemDTO;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.typeMap(OrderItem.class, OrderItemDTO.class).addMappings(mapper -> {
            mapper.map(OrderItem::getBook, OrderItemDTO::setBookDTO);
        });

        return modelMapper;
    }

    
}
