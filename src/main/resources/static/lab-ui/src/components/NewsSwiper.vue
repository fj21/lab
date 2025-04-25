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
        @swiper="onSwiper"
        @slideChange="onSlideChange"
      >
        <swiper-slide v-for="(items, tabIndex) in tabsContent" :key="tabIndex">
          <div class="news-list">
            <div
              v-for="(item, index) in items.slice(0, 3)"
              :key="index"
              class="news-item"
              @click="viewNewsDetail(item, tabIndex)"
            >
              <div class="news-date">{{ item.date }}</div>
              <div class="news-title">{{ item.title }}</div>
            </div>
          </div>
        </swiper-slide>
      </swiper>
    </div>

    <div class="more-btn" @click="viewMore">了解详情</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { Swiper, SwiperSlide } from 'swiper/vue';
import { EffectFade } from 'swiper/modules';
import 'swiper/css';
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

const emit = defineEmits(['more', 'viewDetail']);

const activeTab = ref(0);
const swiperInstance = ref(null);
const modules = [EffectFade];

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

// Function to handle clicking on a news item
const viewNewsDetail = (item, tabIndex) => {
  // Get the news type based on tab index
  const typeMap = {
    0: 'news',
    1: 'notice',
    2: 'academic'
  };
  const type = typeMap[tabIndex] || 'news';

  // Emit event to parent component with the news item and type
  emit('viewDetail', { item, type });
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
  width: 100%;
  min-height: 200px;
  max-height: 250px;
  position: relative;
  z-index: 2;
  pointer-events: auto;
}

:deep(.swiper) {
  width: 100%;
  height: 100%;
}

:deep(.swiper-slide) {
  height: auto;
  pointer-events: auto;
}

.news-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
  position: relative;
  z-index: 5;
  pointer-events: auto;
}

.news-item {
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  flex-direction: column;
  padding: 12px 10px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  position: relative;
  overflow: hidden;
  z-index: 10;
  pointer-events: auto;
}

.news-item:last-child {
  border-bottom: none;
  margin-bottom: 5px;
}

.news-item:hover {
  background-color: rgba(138, 43, 226, 0.1);
}

.news-date {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.6);
  margin-bottom: 6px;
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
  margin-top: 10px;
  padding: 8px 20px;
  background-color: transparent;
  border: 1px solid rgba(255, 255, 255, 0.3);
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 14px;
  border-radius: 4px;
  align-self: center;
}

.more-btn:hover {
  background-color: rgba(138, 43, 226, 0.7);
  border-color: rgba(138, 43, 226, 0.7);
}


</style>
