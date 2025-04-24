<template>
  <div class="teams-container">
    <div class="teams-header">
      <h1>{{ currentLang === 'zh' ? '团队成员' : 'Team Members' }}</h1>
      <p>{{ currentLang === 'zh' ? '我们的研究团队由一群充满激情和创造力的专家组成' : 'Our research team consists of passionate and creative experts' }}</p>
    </div>

    <div class="team-categories">
      <div 
        v-for="category in categories" 
        :key="category.id"
        class="category-item"
        :class="{ active: selectedCategory === category.id }"
        @click="selectedCategory = category.id"
      >
        {{ currentLang === 'zh' ? category.name : category.nameEn }}
      </div>
    </div>

    <div class="team-members">
      <div v-for="member in filteredMembers" :key="member.id" class="member-card">
        <div class="member-photo">
          <img :src="member.photo" :alt="member.name" />
        </div>
        <div class="member-info">
          <h3>{{ currentLang === 'zh' ? member.name : member.nameEn }}</h3>
          <p class="member-title">{{ currentLang === 'zh' ? member.title : member.titleEn }}</p>
          <p class="member-degree">{{ currentLang === 'zh' ? member.degree : member.degreeEn }}</p>
          <p class="member-affiliation">{{ currentLang === 'zh' ? member.affiliation : member.affiliationEn }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';

// Language state
const currentLang = ref(localStorage.getItem('language') || 'zh');

// Team categories
const categories = [
  { id: 'all', name: '全部', nameEn: 'All' },
  { id: 'faculty', name: '教师', nameEn: 'Faculty' },
  { id: 'postdoc', name: '博士后', nameEn: 'Postdocs' },
  { id: 'phd', name: '博士生', nameEn: 'PhD Students' },
  { id: 'master', name: '硕士生', nameEn: 'Master Students' },
  { id: 'alumni', name: '校友', nameEn: 'Alumni' }
];

// Selected category
const selectedCategory = ref('all');

// Team members data (mock data)
const members = ref([
  {
    id: 1,
    name: '陈明',
    nameEn: 'Ming Chen',
    title: '教授，博士生导师',
    titleEn: 'Professor, PhD Supervisor',
    degree: '博士',
    degreeEn: 'PhD',
    affiliation: '计算机科学与工程系',
    affiliationEn: 'Department of Computer Science and Engineering',
    photo: 'https://randomuser.me/api/portraits/men/1.jpg',
    category: 'faculty'
  },
  {
    id: 2,
    name: '李华',
    nameEn: 'Hua Li',
    title: '副教授，博士生导师',
    titleEn: 'Associate Professor, PhD Supervisor',
    degree: '博士',
    degreeEn: 'PhD',
    affiliation: '人工智能研究院',
    affiliationEn: 'Institute of Artificial Intelligence',
    photo: 'https://randomuser.me/api/portraits/women/2.jpg',
    category: 'faculty'
  },
  {
    id: 3,
    name: '张伟',
    nameEn: 'Wei Zhang',
    title: '助理教授',
    titleEn: 'Assistant Professor',
    degree: '博士',
    degreeEn: 'PhD',
    affiliation: '计算机科学与工程系',
    affiliationEn: 'Department of Computer Science and Engineering',
    photo: 'https://randomuser.me/api/portraits/men/3.jpg',
    category: 'faculty'
  },
  {
    id: 4,
    name: '王芳',
    nameEn: 'Fang Wang',
    title: '博士后研究员',
    titleEn: 'Postdoctoral Researcher',
    degree: '博士',
    degreeEn: 'PhD',
    affiliation: '人工智能研究院',
    affiliationEn: 'Institute of Artificial Intelligence',
    photo: 'https://randomuser.me/api/portraits/women/4.jpg',
    category: 'postdoc'
  },
  {
    id: 5,
    name: '赵强',
    nameEn: 'Qiang Zhao',
    title: '博士后研究员',
    titleEn: 'Postdoctoral Researcher',
    degree: '博士',
    degreeEn: 'PhD',
    affiliation: '计算机科学与工程系',
    affiliationEn: 'Department of Computer Science and Engineering',
    photo: 'https://randomuser.me/api/portraits/men/5.jpg',
    category: 'postdoc'
  },
  {
    id: 6,
    name: '钱明',
    nameEn: 'Ming Qian',
    title: '博士研究生',
    titleEn: 'PhD Student',
    degree: '硕士',
    degreeEn: 'Master',
    affiliation: '计算机科学与工程系',
    affiliationEn: 'Department of Computer Science and Engineering',
    photo: 'https://randomuser.me/api/portraits/men/6.jpg',
    category: 'phd'
  },
  {
    id: 7,
    name: '孙丽',
    nameEn: 'Li Sun',
    title: '博士研究生',
    titleEn: 'PhD Student',
    degree: '硕士',
    degreeEn: 'Master',
    affiliation: '人工智能研究院',
    affiliationEn: 'Institute of Artificial Intelligence',
    photo: 'https://randomuser.me/api/portraits/women/7.jpg',
    category: 'phd'
  },
  {
    id: 8,
    name: '周杰',
    nameEn: 'Jie Zhou',
    title: '硕士研究生',
    titleEn: 'Master Student',
    degree: '学士',
    degreeEn: 'Bachelor',
    affiliation: '计算机科学与工程系',
    affiliationEn: 'Department of Computer Science and Engineering',
    photo: 'https://randomuser.me/api/portraits/men/8.jpg',
    category: 'master'
  },
  {
    id: 9,
    name: '吴婷',
    nameEn: 'Ting Wu',
    title: '硕士研究生',
    titleEn: 'Master Student',
    degree: '学士',
    degreeEn: 'Bachelor',
    affiliation: '人工智能研究院',
    affiliationEn: 'Institute of Artificial Intelligence',
    photo: 'https://randomuser.me/api/portraits/women/9.jpg',
    category: 'master'
  },
  {
    id: 10,
    name: '郑阳',
    nameEn: 'Yang Zheng',
    title: '校友，现就职于谷歌',
    titleEn: 'Alumni, now at Google',
    degree: '博士',
    degreeEn: 'PhD',
    affiliation: '2018届毕业',
    affiliationEn: 'Class of 2018',
    photo: 'https://randomuser.me/api/portraits/men/10.jpg',
    category: 'alumni'
  },
  {
    id: 11,
    name: '冯雪',
    nameEn: 'Xue Feng',
    title: '校友，现就职于微软',
    titleEn: 'Alumni, now at Microsoft',
    degree: '博士',
    degreeEn: 'PhD',
    affiliation: '2019届毕业',
    affiliationEn: 'Class of 2019',
    photo: 'https://randomuser.me/api/portraits/women/11.jpg',
    category: 'alumni'
  },
  {
    id: 12,
    name: '陈浩',
    nameEn: 'Hao Chen',
    title: '校友，现就职于百度',
    titleEn: 'Alumni, now at Baidu',
    degree: '硕士',
    degreeEn: 'Master',
    affiliation: '2020届毕业',
    affiliationEn: 'Class of 2020',
    photo: 'https://randomuser.me/api/portraits/men/12.jpg',
    category: 'alumni'
  }
]);

// Filtered members based on selected category
const filteredMembers = computed(() => {
  if (selectedCategory.value === 'all') {
    return members.value;
  }
  return members.value.filter(member => member.category === selectedCategory.value);
});

// Setup
onMounted(() => {
  // Check for language changes
  window.addEventListener('storage', (event) => {
    if (event.key === 'language') {
      currentLang.value = event.newValue;
    }
  });
});
</script>

<style scoped>
.teams-container {
  min-height: 100vh;
  background-color: #0d1117;
  color: #e6edf3;
  padding: 80px 20px 40px;
}

.teams-header {
  text-align: center;
  margin-bottom: 40px;
}

.teams-header h1 {
  font-size: 2.5rem;
  margin-bottom: 10px;
  color: #fff;
}

.teams-header p {
  font-size: 1.2rem;
  color: #8b949e;
}

.team-categories {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 15px;
  margin-bottom: 40px;
}

.category-item {
  padding: 10px 20px;
  background-color: #161b22;
  border-radius: 30px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 0.9rem;
}

.category-item:hover {
  background-color: #21262d;
}

.category-item.active {
  background-color: #8957e5;
  color: #fff;
}

.team-members {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 30px;
  max-width: 1200px;
  margin: 0 auto;
}

.member-card {
  background-color: #161b22;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  transition: transform 0.3s, box-shadow 0.3s;
}

.member-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.3);
}

.member-photo img {
  width: 100%;
  height: 280px;
  object-fit: cover;
}

.member-info {
  padding: 20px;
}

.member-info h3 {
  font-size: 1.2rem;
  margin-bottom: 10px;
  color: #fff;
}

.member-title {
  font-size: 0.9rem;
  color: #8957e5;
  margin-bottom: 5px;
}

.member-degree, .member-affiliation {
  font-size: 0.85rem;
  color: #8b949e;
  margin-bottom: 5px;
}

/* Responsive styles */
@media (max-width: 768px) {
  .teams-header h1 {
    font-size: 2rem;
  }
  
  .team-categories {
    flex-direction: column;
    align-items: center;
  }
  
  .category-item {
    width: 100%;
    max-width: 300px;
    text-align: center;
  }
}
</style>
