// XỬ LÝ CHO TRANG CHỦ / DANH MỤC (Thêm nhanh số lượng 1)
function addToCartHome(variantId) {
    const csrfToken = document.querySelector("meta[name='_csrf']")?.getAttribute("content");
    const csrfHeader = document.querySelector("meta[name='_csrf_header']")?.getAttribute("content");

    fetch(`/cart/add?variantId=${variantId}&quantity=1`, {
        method: 'POST',
        headers: {
            [csrfHeader]: csrfToken
        }
    })
        .then(response => {
            if (response.status === 401) {
                alert("Vui lòng đăng nhập để mua hàng!");
                window.location.href = '/login';
                return;
            }
            return response.json();
        })
        .then(data => {
            if (data && data.status === 'success') {
                alert(data.message);
                const cartBadge = document.getElementById('cartBadge');
                if (cartBadge && data.calculatedTotal !== undefined) {
                    cartBadge.innerText = data.calculatedTotal;
                }
            } else if (data) {
                alert(data.message);
            }
        })
        .catch(error => console.error('Lỗi:', error));
}


function addToCartDetail() {
    submitCartRequest(false);
}


function buyNow() {
    submitCartRequest(true);
}


function submitCartRequest(isBuyNow) {
    const variantIdInput = document.getElementById('selectedVariantId');
    const stockInput = document.getElementById('selectedVariantStock');


    if (!variantIdInput || !variantIdInput.value) {
        alert("Vui lòng chọn phiên bản sản phẩm!");
        return;
    }


    const currentStock = parseInt(stockInput.value);
    if (currentStock <= 0) {
        alert("Rất tiếc, phiên bản này hiện đã hết hàng!");
        return;
    }

    const variantId = variantIdInput.value;
    const quantity = 1;

    const csrfToken = document.querySelector("meta[name='_csrf']")?.getAttribute("content");
    const csrfHeader = document.querySelector("meta[name='_csrf_header']")?.getAttribute("content");

    fetch(`/cart/add?variantId=${variantId}&quantity=${quantity}`, {
        method: 'POST',
        headers: {
            [csrfHeader]: csrfToken
        }
    })
        .then(response => {
            // Xử lý lỗi đăng nhập (401)
            if (response.status === 401) {
                alert("Vui lòng đăng nhập để mua hàng!");
                window.location.href = '/login';
                return null;
            }

            return response.json().then(data => {
                if (!response.ok) {
                    return { status: 'error', message: data.message || "Không thể thêm sản phẩm vào giỏ hàng!" };
                }
                return data;
            });
        })
        .then(data => {
            if (!data) return;
            if (data.status === 'success') {
                if (isBuyNow) {
                    window.location.href = '/cart';
                } else {
                    alert(data.message);
                    const cartBadge = document.getElementById('cartBadge');
                    if (cartBadge && data.calculatedTotal !== undefined) {
                        cartBadge.innerText = data.calculatedTotal;
                    }
                }
            } else {
                alert("⚠️ " + data.message);
            }
        })
        .catch(error => {
            console.error('Lỗi kết nối:', error);
            alert("Đã xảy ra lỗi kết nối tới máy chủ. Vui lòng thử lại sau!");
        });
}


function selectVariant(button) {
    // 1. Gỡ class 'active' khỏi tất cả các nút và gán cho nút vừa bấm (Code cũ của em)
    document.querySelectorAll('.variant-btn').forEach(btn => btn.classList.remove('active'));
    button.classList.add('active');

    // 2. Lấy data từ nút được bấm
    let id = button.getAttribute("data-id");
    let price = button.getAttribute("data-price");
    let stock = parseInt(button.getAttribute("data-stock")); // Chuyển sang số nguyên để kiểm tra

    // 3. Cập nhật các input ẩn (Code cũ của em)
    document.getElementById("selectedVariantId").value = id;
    document.getElementById("selectedVariantStock").value = stock;

    // 4. Cập nhật Giá hiển thị (Code cũ của em)
    let displayPrice = document.getElementById("displayPrice");
    if (displayPrice) {
        displayPrice.innerText = new Intl.NumberFormat('vi-VN').format(price) + '₫';
    }

    // 5. MỚI THÊM: CẬP NHẬT TRẠNG THÁI TỒN KHO TRÊN MÀN HÌNH
    let stockDisplay = document.getElementById("displayStock");
    if (stockDisplay) {
        if (stock > 0) {
            stockDisplay.innerText = "Còn " + stock + " sản phẩm";
            // Đổi màu xanh
            stockDisplay.classList.remove('text-danger');
            stockDisplay.classList.add('text-success');
        } else {
            stockDisplay.innerText = "Hết hàng";
            // Đổi màu đỏ
            stockDisplay.classList.remove('text-success');
            stockDisplay.classList.add('text-danger');
        }
    }
}



function updateQuantity(inputElement) {
    const variantId = inputElement.getAttribute('data-id');
    let quantity = parseInt(inputElement.value);

    if (isNaN(quantity) || quantity < 1) {
        alert("Số lượng sản phẩm ít nhất phải là 1!");
        inputElement.value = 1;
        quantity = 1;
    }

    const csrfToken = document.querySelector("meta[name='_csrf']")?.getAttribute("content");
    const csrfHeader = document.querySelector("meta[name='_csrf_header']")?.getAttribute("content");

    fetch(`/cart/update?variantId=${variantId}&quantity=${quantity}`, {
        method: 'POST',
        headers: {
            [csrfHeader]: csrfToken
        }
    })
        .then(response => {
            if (response.status === 401) {
                alert("Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại!");
                window.location.href = '/login';
                return null;
            }
            return response.json();
        })
        .then(data => {
            if (data && data.status === 'success') {
                window.location.reload();
            } else if (data) {
                alert(data.message);
                window.location.reload();
            }
        })
        .catch(error => console.error('Lỗi khi cập nhật số lượng:', error));
}