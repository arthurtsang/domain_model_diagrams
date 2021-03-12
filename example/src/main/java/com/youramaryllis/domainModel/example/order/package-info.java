@BoundedContext("Order")
@AntiCorruptionLayer("com.youramaryllis.domainModel.example.catalog")
@SharedKernel("com.youramaryllis.domainModel.example.user")
package com.youramaryllis.domainModel.example.order;

import com.youramaryllis.ddd.contextMap.annotations.AntiCorruptionLayer;
import com.youramaryllis.ddd.contextMap.annotations.BoundedContext;
import com.youramaryllis.ddd.contextMap.annotations.SharedKernel;