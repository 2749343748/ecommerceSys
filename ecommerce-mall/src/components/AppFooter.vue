<template>
  <footer class="footer">
    <div class="container">
      <div class="footer-content">
        <div class="footer-section">
          <h4>关于我们</h4>
          <p>品质商城致力于为用户提供高品质的购物体验，精选全球好物，品质保证。</p>
        </div>
        <div class="footer-section">
          <h4>购物指南</h4>
          <ul>
            <li><a href="#">如何下单</a></li>
            <li><a href="#">支付方式</a></li>
            <li><a href="#">配送说明</a></li>
            <li><a href="#">售后服务</a></li>
          </ul>
        </div>
        <div class="footer-section">
          <h4>客服服务</h4>
          <ul>
            <li><a href="#">在线客服</a></li>
            <li><a href="#">投诉建议</a></li>
            <li><a href="#">退换货政策</a></li>
          </ul>
        </div>
        <div class="footer-section">
          <h4>联系我们</h4>
          <p>客服电话：400-888-8888</p>
          <p>工作时间：9:00-21:00</p>
          <p>邮箱：service@mall.com</p>
        </div>
      </div>
      <div class="footer-bottom">
        <p>© 2024 品质商城 版权所有</p>
      </div>
    </div>
  </footer>
</template>

<style scoped>
.footer {
  background: #2c2c2c;
  color: #999;
  padding: 40px 0 20px;
  margin-top: 60px;
}

.footer-content {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 40px;
}

.footer-section h4 {
  color: #fff;
  font-size: 16px;
  margin-bottom: 16px;
}

.footer-section p {
  line-height: 1.8;
  font-size: 13px;
}

.footer-section ul {
  list-style: none;
}

.footer-section li {
  margin-bottom: 8px;
}

.footer-section a {
  color: #999;
  text-decoration: none;
  font-size: 13px;
}

.footer-section a:hover {
  color: #fff;
}

.footer-bottom {
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid #3c3c3c;
  text-align: center;
  font-size: 13px;
}
</style>
