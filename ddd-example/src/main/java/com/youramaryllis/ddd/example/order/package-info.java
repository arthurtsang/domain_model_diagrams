@BoundedContext("Order")
@AntiCorruptionLayer("com.youramaryllis.ddd.example.catalog")
@SharedKernel("com.youramaryllis.ddd.example.user")
package com.youramaryllis.ddd.example.order;

import com.youramaryllis.ddd.contextMap.annotations.AntiCorruptionLayer;
import com.youramaryllis.ddd.contextMap.annotations.BoundedContext;
import com.youramaryllis.ddd.contextMap.annotations.SharedKernel;