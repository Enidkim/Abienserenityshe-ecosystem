
// Mobile nav toggle
document.addEventListener('click', (e) => {
  const toggle = e.target.closest('.mobile-toggle');
  if (toggle) {
    const nav = document.querySelector('.navlinks');
    if (nav) nav.style.display = (nav.style.display === 'flex' ? 'none' : 'flex');
  }
});
const SR_KEY = 'sr_cart';
const WL_KEY = 'sr_wishlist';
function getStore(key){ try { return JSON.parse(localStorage.getItem(key) || '[]'); } catch(e){ return []; } }
function setStore(key, value){ localStorage.setItem(key, JSON.stringify(value)); }
function addToCart(product){
  const cart = getStore(SR_KEY);
  const existing = cart.find(i => i.id === product.id);
  if(existing){ existing.qty += 1; } else { cart.push({...product, qty: 1}); }
  setStore(SR_KEY, cart); alert('Added to cart: ' + product.title); updateCartBadge();
}
function addToWishlist(product){
  const list = getStore(WL_KEY);
  if(!list.find(i => i.id === product.id)){ list.push(product); setStore(WL_KEY, list); alert('Saved ❤️ ' + product.title); }
}
function loadCartTable(){
  const container = document.getElementById('cart-table');
  if(!container) return;
  const cart = getStore(SR_KEY);
  if(cart.length === 0){ container.innerHTML = '<p>Your cart is empty.</p>'; return; }
  let html = '<table class="table"><thead><tr><th>Item</th><th>Qty</th><th>Price</th><th>Total</th><th></th></tr></thead><tbody>';
  let total = 0;
  cart.forEach((item, idx) => {
    const line = item.qty * item.price; total += line;
    html += `<tr><td>${item.title}</td>
             <td><button onclick="decQty(${idx})">-</button> ${item.qty} <button onclick="incQty(${idx})">+</button></td>
             <td>₹${item.price.toLocaleString()}</td>
             <td>₹${line.toLocaleString()}</td>
             <td><button onclick="removeItem(${idx})">Remove</button></td></tr>`;
  });
  html += `</tbody></table><h3>Grand Total: ₹${total.toLocaleString()}</h3>
           <div class="cta"><a class="btn teal" href="contact.html">Checkout via WhatsApp / Email</a>
           <button class="btn outline" onclick="clearCart()">Clear Cart</button></div>`;
  container.innerHTML = html;
}
function incQty(i){ const cart = getStore(SR_KEY); cart[i].qty++; setStore(SR_KEY, cart); loadCartTable(); updateCartBadge(); }
function decQty(i){ const cart = getStore(SR_KEY); cart[i].qty = Math.max(1, cart[i].qty-1); setStore(SR_KEY, cart); loadCartTable(); updateCartBadge(); }
function removeItem(i){ const cart = getStore(SR_KEY); cart.splice(i,1); setStore(SR_KEY, cart); loadCartTable(); updateCartBadge(); }
function clearCart(){ setStore(SR_KEY, []); loadCartTable(); updateCartBadge(); }
function updateCartBadge(){ const badge = document.querySelector('[data-cart-badge]'); if(!badge) return; const cart = getStore(SR_KEY); const count = cart.reduce((a,b)=>a+b.qty,0); badge.textContent = count > 0 ? count : ''; }
document.addEventListener('DOMContentLoaded', updateCartBadge);
document.addEventListener('DOMContentLoaded', loadCartTable);
function filterResources(){ const q = (document.getElementById('res-search')?.value || '').toLowerCase(); document.querySelectorAll('[data-res-card]').forEach(card => { const text = card.textContent.toLowerCase(); card.style.display = text.includes(q) ? '' : 'none'; }); }
