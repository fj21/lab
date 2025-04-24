/**
 * Mock data service for development
 * This file provides fake data for the front-end when backend is not available
 */

// Mock user data
export const mockUser = {
  id: 1,
  username: 'LabUser',
  phone: '13800138000',
  image: 'https://randomuser.me/api/portraits/men/1.jpg',
  signature: '探索科学的奥秘，分享研究的乐趣',
  attention: 120,
  followers: 85,
  works: 32,
  likes: 256
};

// Mock posts data
export const mockPosts = [
  {
    id: 101,
    userId: 1,
    content: '今天在实验室完成了量子计算模型的初步设计，非常兴奋！这将为我们的研究带来新的突破。',
    category: 1,
    mediaType: 0, // 图片类型
    likeCount: 42,
    collectCount: 15,
    commentCount: 8,
    viewCount: 156,
    createdAt: '2023-06-15T09:30:00',
    updatedAt: '2023-06-15T09:30:00',
    coverUrl: 'https://images.unsplash.com/photo-1581093458791-9d15482442f5?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80',
    mediaUrls: [
      'https://images.unsplash.com/photo-1581093458791-9d15482442f5?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80',
      'https://images.unsplash.com/photo-1532094349884-543bc11b234d?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80'
    ],
    userBasicVO: {
      id: 1,
      username: 'LabUser',
      image: 'https://randomuser.me/api/portraits/men/1.jpg',
      signature: '探索科学的奥秘，分享研究的乐趣'
    }
  },
  {
    id: 102,
    userId: 2,
    content: '分享一下我们实验室最新的材料科学研究成果，这种新型纳米材料具有优异的导电性和强度。',
    category: 2,
    mediaType: 0, // 图片类型
    likeCount: 38,
    collectCount: 12,
    commentCount: 5,
    viewCount: 142,
    createdAt: '2023-06-14T14:20:00',
    updatedAt: '2023-06-14T14:20:00',
    coverUrl: 'https://images.unsplash.com/photo-1614935151651-0bea6508db6b?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1225&q=80',
    mediaUrls: [
      'https://images.unsplash.com/photo-1614935151651-0bea6508db6b?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1225&q=80'
    ],
    userBasicVO: {
      id: 2,
      username: 'MaterialScientist',
      image: 'https://randomuser.me/api/portraits/women/2.jpg',
      signature: '材料科学研究者，致力于新型材料的开发'
    }
  },
  {
    id: 103,
    userId: 3,
    content: '实验室安全培训日，每个人都需要了解基本的安全知识和应急处理流程。安全第一！',
    category: 3,
    mediaType: 1, // 视频类型
    likeCount: 56,
    collectCount: 23,
    commentCount: 14,
    viewCount: 210,
    createdAt: '2023-06-13T10:15:00',
    updatedAt: '2023-06-13T10:15:00',
    coverUrl: 'https://images.unsplash.com/photo-1581091226825-a6a2a5aee158?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80',
    mediaUrls: [
      'https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_1mb.mp4'
    ],
    userBasicVO: {
      id: 3,
      username: 'SafetyFirst',
      image: 'https://randomuser.me/api/portraits/men/3.jpg',
      signature: '实验室安全管理员，确保每个人的安全'
    }
  },
  {
    id: 104,
    userId: 4,
    content: '今天的生物实验取得了重大突破，我们成功培养出了具有特定功能的干细胞。这将为再生医学带来新的可能！',
    category: 1,
    mediaType: 0, // 图片类型
    likeCount: 78,
    collectCount: 34,
    commentCount: 22,
    viewCount: 320,
    createdAt: '2023-06-12T16:45:00',
    updatedAt: '2023-06-12T16:45:00',
    coverUrl: 'https://images.unsplash.com/photo-1576086213369-97a306d36557?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1180&q=80',
    mediaUrls: [
      'https://images.unsplash.com/photo-1576086213369-97a306d36557?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1180&q=80',
      'https://images.unsplash.com/photo-1530210124550-912dc1381cb8?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80'
    ],
    userBasicVO: {
      id: 4,
      username: 'BiologyPro',
      image: 'https://randomuser.me/api/portraits/women/4.jpg',
      signature: '生物学研究者，专注于干细胞研究'
    }
  },
  {
    id: 105,
    userId: 5,
    content: '实验室新设备到货啦！这台高性能计算集群将大大提升我们的数据处理能力。',
    category: 2,
    mediaType: 0, // 图片类型
    likeCount: 45,
    collectCount: 18,
    commentCount: 9,
    viewCount: 180,
    createdAt: '2023-06-11T11:30:00',
    updatedAt: '2023-06-11T11:30:00',
    coverUrl: 'https://images.unsplash.com/photo-1517694712202-14dd9538aa97?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80',
    mediaUrls: [
      'https://images.unsplash.com/photo-1517694712202-14dd9538aa97?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80',
      'https://images.unsplash.com/photo-1555255707-c07966088b7b?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1032&q=80'
    ],
    userBasicVO: {
      id: 5,
      username: 'TechGeek',
      image: 'https://randomuser.me/api/portraits/men/5.jpg',
      signature: '技术爱好者，热衷于高性能计算'
    }
  }
];

// Mock comments data
export const mockComments = [
  {
    id: 201,
    postId: 101,
    userId: 2,
    parentId: 0,
    content: '这个研究非常有前景，期待看到更多进展！',
    likedNum: 12,
    createTime: '2023-06-15T10:15:00',
    hasChildComment: true,
    childCommentNum: 2,
    userBasicVO: {
      id: 2,
      username: 'MaterialScientist',
      image: 'https://randomuser.me/api/portraits/women/2.jpg',
      signature: '材料科学研究者，致力于新型材料的开发'
    }
  },
  {
    id: 202,
    postId: 101,
    userId: 3,
    parentId: 0,
    content: '量子计算是未来的趋势，你们的研究很有意义。',
    likedNum: 8,
    createTime: '2023-06-15T11:20:00',
    hasChildComment: false,
    childCommentNum: 0,
    userBasicVO: {
      id: 3,
      username: 'SafetyFirst',
      image: 'https://randomuser.me/api/portraits/men/3.jpg',
      signature: '实验室安全管理员，确保每个人的安全'
    }
  },
  {
    id: 203,
    postId: 101,
    userId: 4,
    parentId: 201,
    content: '我也很期待，这个领域发展迅速！',
    likedNum: 5,
    createTime: '2023-06-15T12:30:00',
    hasChildComment: false,
    childCommentNum: 0,
    userBasicVO: {
      id: 4,
      username: 'BiologyPro',
      image: 'https://randomuser.me/api/portraits/women/4.jpg',
      signature: '生物学研究者，专注于干细胞研究'
    }
  }
];

// Mock categories
export const mockCategories = [
  { id: 0, name: '全部' },
  { id: 1, name: '研究成果' },
  { id: 2, name: '设备资源' },
  { id: 3, name: '实验室生活' },
  { id: 4, name: '学术交流' }
];

// Mock API response for posts
export function getMockPosts(category = null, lastPostId = 0) {
  let filteredPosts = [...mockPosts];
  
  // Filter by category if provided
  if (category !== null && category !== 0) {
    filteredPosts = filteredPosts.filter(post => post.category === category);
  }
  
  // Filter by lastPostId
  if (lastPostId > 0) {
    filteredPosts = filteredPosts.filter(post => post.id < lastPostId);
  }
  
  // Sort by creation date (newest first)
  filteredPosts.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
  
  // Limit to 50 posts
  filteredPosts = filteredPosts.slice(0, 50);
  
  return {
    code: 200,
    message: 'success',
    data: {
      postVOList: filteredPosts,
      lastPostId: filteredPosts.length > 0 ? filteredPosts[filteredPosts.length - 1].id : 0
    }
  };
}

// Mock API response for post detail
export function getMockPostDetail(postId) {
  const post = mockPosts.find(p => p.id === parseInt(postId));
  
  if (!post) {
    return {
      code: 404,
      message: 'Post not found',
      data: null
    };
  }
  
  // Get comments for this post
  const postComments = mockComments.filter(c => c.postId === parseInt(postId) && c.parentId === 0);
  
  const postDetail = {
    id: post.id,
    authorId: post.userId,
    content: post.content,
    category: post.category,
    mediaType: post.mediaType,
    likeCount: post.likeCount,
    collectCount: post.collectCount,
    commentCount: post.commentCount,
    viewCount: post.viewCount + 1, // Increment view count
    createdAt: post.createdAt,
    coverUrl: post.coverUrl,
    mediaVOList: post.mediaUrls.map((url, index) => ({
      mediaUrl: url,
      sortOrder: index
    })),
    commentVOList: postComments,
    lastCommentId: postComments.length > 0 ? postComments[postComments.length - 1].id : 0,
    userBasicVO: post.userBasicVO
  };
  
  return {
    code: 200,
    message: 'success',
    data: postDetail
  };
}

// Mock API response for user info
export function getMockUserInfo() {
  return {
    code: 200,
    message: 'success',
    data: mockUser
  };
}
