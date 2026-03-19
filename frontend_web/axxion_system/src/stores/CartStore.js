import { defineStore } from 'pinia';
import { ref, computed } from 'vue';

export const useCartStore = defineStore('cart', () => {
  const items = ref([]);
  const isOpen = ref(false);

  const addToCart = (product) => {
    const existingItem = items.value.find(item => item.id === product.id);
    if (existingItem) {
      // If item exists, maybe increment quantity or just notify it's already there
      // For rentals, usually quantity is 1 per unique asset unless it's a bulk item
      // Assuming bulk for now based on "cantidad" in DB
      existingItem.quantity = (existingItem.quantity || 1) + 1;
    } else {
      items.value.push({
        ...product,
        quantity: 1
      });
    }
    isOpen.value = true;
  };

  const removeFromCart = (productId) => {
    items.value = items.value.filter(item => item.id !== productId);
  };

  const updateQuantity = (productId, quantity) => {
    const item = items.value.find(item => item.id === productId);
    if (item) {
      item.quantity = quantity;
      if (item.quantity <= 0) {
        removeFromCart(productId);
      }
    }
  };

  const clearCart = () => {
    items.value = [];
  };

  const toggleCart = () => {
    isOpen.value = !isOpen.value;
  };

  const openCart = () => {
    isOpen.value = true;
  };

  const closeCart = () => {
    isOpen.value = false;
  };

  const totalPrice = computed(() => {
    return items.value.reduce((total, item) => {
      // Prioritize rental price, fallback to reference price or 0
      const price = parseFloat(item.precio_alquiler_dia || item.precio_referencia_renta || 0);
      return total + (price * (item.quantity || 1));
    }, 0);
  });

  const itemCount = computed(() => {
    return items.value.reduce((count, item) => count + (item.quantity || 1), 0);
  });

  return {
    items,
    isOpen,
    addToCart,
    removeFromCart,
    updateQuantity,
    clearCart,
    toggleCart,
    openCart,
    closeCart,
    totalPrice,
    itemCount
  };
});
