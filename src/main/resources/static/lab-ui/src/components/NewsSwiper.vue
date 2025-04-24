<template>
  <div class="news-swiper">
    <div class="tabs">
      <div
        v-for="(tab, index) in tabs"
        :key="index"
        :class="['tab', { active: activeTab === index }]"
        @click="setActiveTab(index)"
      >
        {{ tab }}
      </div>
    </div>

    <div class="swiper-container">
      <swiper
        :slides-per-view="1"
        :space-between="30"
        :modules="modules"
        :pagination="{ clickable: true }"
        @swiper="onSwiper"
        @slideChange="onSlideChange"
      >
        <swiper-slide v-for="(items, tabIndex) in tabsContent" :key="tabIndex">
          <div class="news-list">
            <div v-for="(item, index) in items" :key="index" class="news-item">
              <div class="news-date">{{ item.date }}</div>
              <div class="news-title">{{ item.title }}</div>
            </div>
          </div>
        </swiper-slide>
      </swiper>
    </div>

    <div class="more-btn" @click="viewMore">了解更多</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { Swiper, SwiperSlide } from 'swiper/vue';
import { Pagination, EffectFade } from 'swiper/modules';
import 'swiper/css';
import 'swiper/css/pagination';
import 'swiper/css/effect-fade';

const props = defineProps({
  tabs: {
    type: Array,
    default: () => ['新闻动态', '通知公告', '学术动态']
  },
  tabsContent: {
    type: Array,
    default: () => []
  }
});

const emit = defineEmits(['more']);

const activeTab = ref(0);
const swiperInstance = ref(null);
const modules = [Pagination, EffectFade];

const onSwiper = (swiper) => {
  swiperInstance.value = swiper;
};

const onSlideChange = () => {
  if (swiperInstance.value) {
    activeTab.value = swiperInstance.value.activeIndex;
  }
};

const setActiveTab = (index) => {
  activeTab.value = index;
  if (swiperInstance.value) {
    swiperInstance.value.slideTo(index);
  }
};

const viewMore = () => {
  emit('more', activeTab.value);
};
</script>

<style scoped>
.news-swiper {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  color: #ffffff;
}

.tabs {
  display: flex;
  gap: 20px;
  margin-bottom: 30px;
}

.tab {
  font-size: 16px;
  cursor: pointer;
  padding-bottom: 5px;
  position: relative;
  color: rgba(255, 255, 255, 0.6);
  transition: color 0.3s;
}

.tab.active {
  color: #ffffff;
}

.tab.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 2px;
  background-color: #8a2be2;
}

.swiper-container {
  flex: 1;
  width: 100%;
}

:deep(.swiper) {
  width: 100%;
  height: 100%;
}

:deep(.swiper-slide) {
  height: auto;
}

.news-list {
  display: flex;
  flex-direction: column;
  gap: 25px;
}

.news-item {
  cursor: pointer;
  transition: transform 0.3s;
}

.news-item:hover {
  transform: translateX(5px);
}

.news-date {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.6);
  margin-bottom: 5px;
}

.news-title {
  font-size: 14px;
  line-height: 1.5;
  transition: color 0.3s;
}

.news-item:hover .news-title {
  color: #8a2be2;
}

.more-btn {
  margin-top: 30px;
  padding: 10px 20px;
  border: 1px solid rgba(255, 255, 255, 0.3);
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 14px;
  border-radius: 4px;
}

.more-btn:hover {
  background-color: rgba(138, 43, 226, 0.7);
  border-color: rgba(138, 43, 226, 0.7);
}

:deep(.swiper-pagination) {
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
  display: flex;
  flex-direction: column;
  width: auto;
  height: auto;
}

:deep(.swiper-pagination-bullet) {
  width: 8px;
  height: 8px;
  margin: 5px 0;
  background-color: #555555;
  opacity: 1;
}

:deep(.swiper-pagination-bullet-active) {
  background-color: #ffffff;
  height: 20px;
  border-radius: 10px;
}
</style>
