# 🐳 Community Platform API - Docker 테스트 가이드

## 📚 프로젝트 개요

### 시스템 아키텍처
**Domain-Driven Design (DDD) 기반 커뮤니티 플랫폼 API**

```
├── 📦 Domain Layer (도메인 계층)
│   ├── User (사용자): 회원가입, 인증, 프로필 관리
│   ├── Content (콘텐츠): 게시글, 댓글, 카테고리, 태그
│   ├── Engagement (참여): 좋아요, 스크랩, 스크랩 폴더
│   ├── Notification (알림): 시스템 알림
│   ├── Authorization (권한): 역할 기반 접근 제어 (RBAC)
│   ├── Reward (보상): 포인트 시스템
│   └── Moderation (중재): 신고 시스템
│
├── 🔧 Application Layer (응용 계층)
│   └── Service 클래스들 (비즈니스 로직 처리)
│
├── 🌐 Presentation Layer (표현 계층)
│   └── REST API Controllers
│
└── 🗄️ Infrastructure Layer (인프라 계층)
    ├── JPA Repository (데이터 영속성)
    ├── QueryDSL (동적 쿼리)
    └── Security (Spring Security + JWT)
```

### 기술 스택
- **Framework**: Spring Boot 3.2.0, Spring Data JPA, Spring Security
- **Database**: H2 (개발), PostgreSQL/MySQL (운영)
- **Auth**: JWT 토큰 기반 인증
- **Object Mapping**: MapStruct, Lombok
- **Build**: Gradle 8.5, Docker
- **Query**: QueryDSL 5.0.0

### 주요 기능
✅ **구현 완료**
- **사용자 관리**: 회원가입, 로그인, JWT 인증
- **게시글 관리**: CRUD, 검색, 발행, 인기글, 트렌딩
- **댓글 관리**: CRUD, 대댓글, 계층형 구조
- **좋아요 관리**: 게시글 좋아요/취소, 좋아요 목록 조회
- **스크랩 관리**: 게시글 스크랩, 스크랩 폴더 관리
- **포인트 시스템**: 포인트 적립/사용, 레벨 시스템, 랭킹 ✨ NEW!
- **역할 기반 접근 제어**: 5단계 역할, 29개 세부 권한, 자동 승격 ✨ NEW!
- **Redis 연결**: AWS ElastiCache 연동 완료
- 카테고리 관리 (계층형 구조)
- 태그 시스템
- Spring Security 설정

✅ **구현 완료**
- 신고 관리 시스템 ✨ NEW!

🚧 **진행 중** (Phase 6)
- 콘텐츠 필터링
- 공지사항 & 큐레이션

🔴 **미구현**
- 알림 시스템
- 실시간 기능

---

## ⚠️ 현재 빌드 상태

### ✅ 모든 주요 API 활성화 완료!
모든 핵심 기능의 컨트롤러가 활성화되어 정상적으로 작동합니다.

### ✅ 테스트 가능한 API

#### 🔐 **인증 API** (AuthController, UserService)
- **POST** `/api/v1/users/register` - 회원가입
- **POST** `/api/v1/auth/login` - 로그인
- **POST** `/api/v1/auth/refresh` - 토큰 갱신
- **POST** `/api/v1/auth/logout` - 로그아웃 (인증 필요)

#### 📝 **게시글 API** (PostController, PostService) ✨ NEW!
- **GET** `/api/v1/posts` - 게시글 목록 조회
- **GET** `/api/v1/posts/{postId}` - 게시글 상세 조회
- **POST** `/api/v1/posts` - 게시글 작성 (인증 필요)
- **POST** `/api/v1/posts/{postId}/publish` - 게시글 발행 (인증 필요)
- **PUT** `/api/v1/posts/{postId}` - 게시글 수정 (인증 필요)
- **DELETE** `/api/v1/posts/{postId}` - 게시글 삭제 (인증 필요)
- **GET** `/api/v1/posts/search` - 게시글 검색
- **GET** `/api/v1/posts/popular` - 인기 게시글 조회
- **GET** `/api/v1/posts/trending` - 트렌딩 게시글 조회
- **GET** `/api/v1/posts/notices` - 공지사항 목록 조회
- **GET** `/api/v1/posts/category/{categoryId}` - 카테고리별 게시글 조회
- **GET** `/api/v1/posts/author/{authorId}` - 작성자별 게시글 조회
- **GET** `/api/v1/posts/{postId}/similar` - 유사 게시글 조회

#### 📁 **카테고리 API** (CategoryService)
- **GET** `/api/v1/categories/tree` - 카테고리 트리 조회
- **GET** `/api/v1/categories/root` - 최상위 카테고리 목록 조회
- **POST** `/api/v1/categories?name={name}&description={desc}` - 카테고리 생성
- **PUT** `/api/v1/categories/{id}?name={name}` - 카테고리 수정

#### 🏷️ **태그 API** (TagService)
- **GET** `/api/v1/tags` - 태그 목록
- **POST** `/api/v1/tags` - 태그 생성

#### 💬 **댓글 API** (CommentController, CommentService)
- **POST** `/api/v1/comments?currentUserId={userId}` - 댓글 작성 (인증 필요)
- **GET** `/api/v1/comments/posts/{postId}` - 게시글의 댓글 목록 조회 (계층형)
- **GET** `/api/v1/comments/posts/{postId}/root` - 게시글의 최상위 댓글만 조회
- **GET** `/api/v1/comments/{commentId}` - 댓글 상세 조회
- **GET** `/api/v1/comments/{parentCommentId}/replies` - 대댓글 목록 조회
- **PUT** `/api/v1/comments/{commentId}?currentUserId={userId}` - 댓글 수정 (인증 필요)
- **DELETE** `/api/v1/comments/{commentId}?currentUserId={userId}` - 댓글 삭제 (인증 필요)
- **GET** `/api/v1/comments/author/{authorId}` - 작성자별 댓글 조회
- **GET** `/api/v1/comments/search?keyword={keyword}` - 댓글 검색
- **GET** `/api/v1/comments/recent` - 최근 댓글 조회 (관리자용)
- **POST** `/api/v1/comments/{commentId}/block` - 댓글 차단 (관리자용)
- **POST** `/api/v1/comments/{commentId}/restore` - 댓글 복원 (관리자용)

#### ❤️ **좋아요 API** (PostLikeController, PostLikeService)
- **POST** `/api/v1/posts/{postId}/like?currentUserId={userId}` - 게시글 좋아요 토글 (인증 필요)
- **GET** `/api/v1/posts/{postId}/like/status?currentUserId={userId}` - 좋아요 상태 확인
- **GET** `/api/v1/posts/{postId}/like/count` - 게시글 좋아요 수 조회
- **GET** `/api/v1/posts/{postId}/likes` - 게시글 좋아요한 사용자 목록 조회
- **GET** `/api/v1/posts/likes/me?currentUserId={userId}` - 내가 좋아요한 게시글 목록 조회

#### 📌 **스크랩 API** (PostScrapController, PostScrapService) ✨ NEW!
- **POST** `/api/v1/posts/{postId}/scrap?currentUserId={userId}` - 게시글 스크랩 (인증 필요)
- **DELETE** `/api/v1/posts/{postId}/scrap?currentUserId={userId}` - 게시글 스크랩 취소 (인증 필요)
- **GET** `/api/v1/posts/{postId}/scrap/status?currentUserId={userId}` - 스크랩 상태 확인
- **GET** `/api/v1/posts/{postId}/scrap/count` - 게시글 스크랩 수 조회
- **GET** `/api/v1/posts/scraps/me?currentUserId={userId}` - 내 스크랩 목록 조회
- **PUT** `/api/v1/posts/{postId}/scrap/move?currentUserId={userId}&targetFolderId={folderId}` - 스크랩 폴더 이동
- **GET** `/api/v1/posts/scraps/me/search?currentUserId={userId}&keyword={keyword}` - 스크랩 검색
- **GET** `/api/v1/posts/scraps/me/recent?currentUserId={userId}&days={days}` - 최근 스크랩 조회

#### 📁 **스크랩 폴더 API** (ScrapFolderController, ScrapFolderService)
- **POST** `/api/v1/scrap-folders?currentUserId={userId}` - 스크랩 폴더 생성 (인증 필요)
- **GET** `/api/v1/scrap-folders/me?currentUserId={userId}` - 내 스크랩 폴더 목록 조회
- **GET** `/api/v1/scrap-folders/{folderId}?currentUserId={userId}` - 스크랩 폴더 상세 조회
- **PUT** `/api/v1/scrap-folders/{folderId}?currentUserId={userId}` - 스크랩 폴더 수정 (인증 필요)
- **DELETE** `/api/v1/scrap-folders/{folderId}?currentUserId={userId}` - 스크랩 폴더 삭제 (인증 필요)
- **GET** `/api/v1/posts/scrap-folders/{folderId}/scraps?currentUserId={userId}` - 특정 폴더의 스크랩 목록 조회
- **GET** `/api/v1/scrap-folders/me/empty?currentUserId={userId}` - 빈 스크랩 폴더 조회
- **POST** `/api/v1/scrap-folders/{folderId}/set-default?currentUserId={userId}` - 기본 폴더 설정

#### 🎁 **포인트 API** (PointController, PointService) ✨ NEW!
- **GET** `/api/v1/points/me?currentUserId={userId}` - 내 포인트 정보 조회
- **GET** `/api/v1/points/me/transactions?currentUserId={userId}` - 내 포인트 거래 내역 조회
- **GET** `/api/v1/points/me/transactions/period?startDate={date}&endDate={date}` - 기간별 거래 내역
- **POST** `/api/v1/points/me/use?currentUserId={userId}` - 포인트 사용 (인증 필요)
- **GET** `/api/v1/points/ranking` - 포인트 랭킹 조회
- **GET** `/api/v1/points/statistics/levels` - 레벨별 사용자 통계
- **GET** `/api/v1/points/statistics/total` - 전체 포인트 통계

#### 🎁 **관리자 포인트 API** ✨ NEW!
- **POST** `/api/v1/points/admin/adjust?currentUserId={adminId}` - 포인트 지급/차감 (관리자 전용)
- **GET** `/api/v1/points/admin/users/{userId}` - 사용자 포인트 조회 (관리자 전용)
- **GET** `/api/v1/points/admin/users/level/{level}` - 레벨별 사용자 조회 (관리자 전용)

#### 👑 **역할 및 권한 API** (RoleController, RoleService) ✨ NEW!
- **GET** `/api/v1/roles` - 모든 역할 정보 조회
- **GET** `/api/v1/roles/{role}` - 특정 역할 정보 조회
- **GET** `/api/v1/roles/permissions` - 모든 권한 정보 조회
- **GET** `/api/v1/roles/{role}/permissions` - 특정 역할의 권한 목록
- **GET** `/api/v1/roles/check-permission?userId={id}&permission={perm}` - 사용자 권한 확인
- **GET** `/api/v1/roles/statistics` - 역할별 사용자 통계

#### 👑 **관리자 역할 관리 API** ✨ NEW!
- **POST** `/api/v1/roles/admin/change?currentUserId={adminId}` - 사용자 역할 변경 (관리자 전용)
- **GET** `/api/v1/roles/admin/{role}/users` - 역할별 사용자 목록 (관리자 전용)
- **GET** `/api/v1/roles/admin/admins` - 관리자 목록 조회 (관리자 전용)

#### 🚨 **신고 API** (ReportController, ReportService) ✨ NEW!
- **POST** `/api/v1/reports?currentUserId={userId}` - 신고 생성 (인증 필요)
- **GET** `/api/v1/reports/{reportId}` - 신고 상세 조회
- **GET** `/api/v1/reports/pending` - 대기 중인 신고 목록 (관리자 전용)
- **GET** `/api/v1/reports/high-severity` - 고위험 신고 목록 (관리자 전용)
- **GET** `/api/v1/reports/status/{status}` - 상태별 신고 목록 (관리자 전용)
- **GET** `/api/v1/reports/type/{targetType}` - 대상 유형별 신고 목록 (관리자 전용)
- **GET** `/api/v1/reports/target/{targetType}/{targetId}` - 특정 대상의 신고 목록
- **GET** `/api/v1/reports/me?currentUserId={userId}` - 내가 신고한 목록 조회
- **GET** `/api/v1/reports/user/{userId}` - 특정 사용자에 대한 신고 목록 (관리자 전용)
- **POST** `/api/v1/reports/{reportId}/review/start?currentUserId={adminId}` - 신고 검토 시작 (관리자 전용)
- **POST** `/api/v1/reports/{reportId}/approve?currentUserId={adminId}` - 신고 승인 (관리자 전용)
- **POST** `/api/v1/reports/{reportId}/reject?currentUserId={adminId}` - 신고 반려 (관리자 전용)
- **GET** `/api/v1/reports/statistics` - 신고 통계 조회 (관리자 전용)
- **GET** `/api/v1/reports/statistics/count?startDate={date}&endDate={date}` - 기간별 신고 수 (관리자 전용)
- **GET** `/api/v1/reports/statistics/target/{targetType}/{targetId}/count` - 대상별 신고 수 조회

#### 🔨 **사용자 제재 API** (PenaltyController, UserPenaltyService) ✨ NEW!
- **POST** `/api/v1/penalties?currentUserId={adminId}` - 수동 제재 부여 (관리자 전용)
- **GET** `/api/v1/penalties/user/{userId}` - 사용자 제재 이력 조회
- **GET** `/api/v1/penalties/user/{userId}/active` - 사용자 활성 제재 조회
- **GET** `/api/v1/penalties/active` - 모든 활성 제재 목록 (관리자 전용)
- **DELETE** `/api/v1/penalties/{penaltyId}?currentUserId={adminId}` - 제재 해제 (관리자 전용)
- **GET** `/api/v1/penalties/users/{userId}/can-post` - 글쓰기 가능 여부 확인
- **GET** `/api/v1/penalties/users/{userId}/can-comment` - 댓글 작성 가능 여부 확인

#### 💊 **Health Check**
- **GET** `/actuator/health` - 서버 상태 확인
- **GET** `/h2-console` - H2 데이터베이스 콘솔

---

## 📋 준비 사항

### 필수 도구
- **curl** 또는 **Postman** (API 테스트용)

### 서버 접속 정보
- **외부 접속 URL**: `http://54.180.251.210:8080`
- **로컬 접속 URL**: `http://localhost:8080`

---

## 🚀 서버 시작하기

### 1. Docker 빌드 및 실행
```bash
cd /home/ec2-user/DDD2

# Docker 이미지 빌드 및 컨테이너 실행
docker-compose up --build -d

# 서버 상태 확인
docker-compose ps
```

### 2. 서버 상태 확인
```bash
# Health Check (로컬)
curl http://localhost:8080/actuator/health

# Health Check (외부)
curl http://54.180.251.210:8080/actuator/health

# 응답 예시:
# {"status":"UP","groups":["liveness","readiness"]}
```

### 3. H2 데이터베이스 콘솔 접속
- **URL**: http://54.180.251.210:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: (비어있음)

---

## 🧪 API 테스트 방법

### 1. 회원가입 테스트

**비밀번호 규칙:**
- 길이: 8~20자
- 필수 포함: 대문자, 소문자, 숫자, 특수문자
- 허용 특수문자: `@ $ ! % * ? &` (주의: `#`는 사용 불가)

```bash
curl -X POST http://54.180.251.210:8080/api/v1/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "Password123@",
    "nickname": "testuser"
  }'
```

**응답 예시 (201 Created):**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "email": "test@example.com",
    "nickname": "testuser",
    "status": "ACTIVE",
    "createdAt": "2025-12-21 09:33:31",
    "lastLoginAt": null,
    "profile": null
  },
  "message": "회원가입이 완료되었습니다",
  "timestamp": "2025-12-21T09:33:31.856449691"
}
```

### 2. 로그인 테스트

```bash
curl -X POST http://54.180.251.210:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "Password123@"
  }'
```

**응답 예시 (200 OK):**
```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMkBleGFtcGxlLmNvbSIsInVzZXJJZCI6MSwidG9rZW5UeXBlIjoiQUNDRVNTIiwiYXV0aCI6IlJPTEVfVVNFUiIsImlhdCI6MTc2NjMwOTg3NiwiZXhwIjoxNzY2MzEzNDc2fQ...",
    "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMkBleGFtcGxlLmNvbSIsInVzZXJJZCI6MSwidG9rZW5UeXBlIjoiUkVGUkVTSCIsImlhdCI6MTc2NjMwOTg3NiwiZXhwIjoxNzY3NTE5NDc2fQ...",
    "tokenType": "Bearer",
    "expiresIn": 3600,
    "user": {
      "id": 1,
      "email": "test@example.com",
      "nickname": "testuser",
      "status": "ACTIVE",
      "createdAt": "2025-12-21 09:37:50",
      "lastLoginAt": null,
      "profile": null
    }
  },
  "message": "로그인이 완료되었습니다",
  "timestamp": "2025-12-21T09:37:56.889171438"
}
```

ADMIN으로 변경하려면 H2 데이터베이스 콘솔에 직접 접속해서 역할을 변경해야 합니다. (최초 관리자는 데이터베이스에서 직접 설정해야 합니다)
```sql
  -- 예: ID가 1인 사용자를 ADMIN으로 변경
  UPDATE users SET role = 'ADMIN' WHERE id = 1;
```

### 3. 인증이 필요한 API 호출 예시

로그인 후 받은 `accessToken`을 사용하여 인증이 필요한 API를 호출할 수 있습니다.

```bash
# 토큰 변수 설정 (로그인 응답에서 받은 토큰)
TOKEN="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."

# 로그아웃 (인증 필요)
curl -X POST http://54.180.251.210:8080/api/v1/auth/logout \
  -H "Authorization: Bearer $TOKEN"
```

### 4. 카테고리 생성 및 조회

**카테고리 생성** (관리자용):
```bash
curl -X POST "http://54.180.251.210:8080/api/v1/categories?name=General&description=General%20discussion%20board"
```

**카테고리 트리 조회** (인증 불필요):
```bash
curl http://54.180.251.210:8080/api/v1/categories/tree
```

### 5. 게시글 작성 테스트 (인증 필요) ✨ NEW!

게시글 작성은 로그인한 사용자만 가능합니다. 먼저 로그인하여 토큰을 받아야 합니다.

```bash
# 1. 로그인하여 토큰 받기
TOKEN=$(curl -s -X POST http://54.180.251.210:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"Password123@"}' \
  | grep -o '"accessToken":"[^"]*' | cut -d'"' -f4)

# 2. 게시글 작성 (currentUserId는 로그인한 사용자의 ID)
curl -X POST "http://54.180.251.210:8080/api/v1/posts?currentUserId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "categoryId": 1,
    "title": "첫 번째 게시글",
    "content": "안녕하세요! 첫 게시글입니다.",
    "contentType": "MARKDOWN",
    "tags": ["테스트", "첫글"]
  }'

# 3. 게시글 발행 (임시저장 → 발행)
curl -X POST "http://54.180.251.210:8080/api/v1/posts/1/publish?currentUserId=1" \
  -H "Authorization: Bearer $TOKEN"
```

**응답 예시 (201 Created):**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "title": "첫 번째 게시글",
    "content": "안녕하세요! 첫 게시글입니다.",
    "status": "DRAFT",
    "viewCount": 0,
    "likeCount": 0,
    "commentCount": 0,
    "createdAt": "2025-12-22 12:00:00",
    "author": {
      "id": 1,
      "nickname": "testuser",
      "email": "test@example.com"
    },
    "category": {
      "id": 1,
      "name": "자유게시판"
    }
  },
  "message": "게시글이 작성되었습니다"
}
```

### 6. 게시글 목록 조회 (인증 불필요)

```bash
# 전체 게시글 목록 (페이징)
curl "http://54.180.251.210:8080/api/v1/posts?page=0&size=20"

# 인기 게시글 조회 (최근 7일)
curl "http://54.180.251.210:8080/api/v1/posts/popular?days=7"

# 트렌딩 게시글 조회 (최근 24시간)
curl "http://54.180.251.210:8080/api/v1/posts/trending?hours=24"

# 게시글 검색
curl "http://54.180.251.210:8080/api/v1/posts/search?keyword=테스트"

# 카테고리별 게시글
curl "http://54.180.251.210:8080/api/v1/posts/category/1"
```

### 7. 게시글 상세 조회 (인증 불필요)

```bash
curl http://54.180.251.210:8080/api/v1/posts/1
```

### 8. 댓글 작성 및 조회 (댓글 기능) ✨ NEW!

**댓글 작성 (인증 필요)**
```bash
# 1. 로그인하여 토큰 받기 (위 5번 참고)
TOKEN=$(curl -s -X POST http://54.180.251.210:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"Password123@"}' \
  | grep -o '"accessToken":"[^"]*' | cut -d'"' -f4)

# 2. 최상위 댓글 작성
curl -X POST "http://54.180.251.210:8080/api/v1/comments?currentUserId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "postId": 1,
    "content": "좋은 게시글이네요! 감사합니다."
  }'

# 3. 대댓글 작성 (parentCommentId 포함)
curl -X POST "http://54.180.251.210:8080/api/v1/comments?currentUserId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "postId": 1,
    "parentCommentId": 1,
    "content": "저도 동감합니다!"
  }'
```

**응답 예시 (201 Created):**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "postId": 1,
    "parentCommentId": null,
    "author": {
      "id": 1,
      "nickname": "testuser",
      "email": "test@example.com"
    },
    "content": "좋은 게시글이네요! 감사합니다.",
    "status": "ACTIVE",
    "likeCount": 0,
    "depth": 0,
    "createdAt": "2025-12-22 12:00:00",
    "updatedAt": "2025-12-22 12:00:00",
    "replies": [],
    "isLikedByUser": false,
    "isAuthor": true
  },
  "message": "댓글이 작성되었습니다"
}
```

**댓글 목록 조회 (인증 불필요)**
```bash
# 게시글의 모든 댓글 조회 (계층형 구조)
curl "http://54.180.251.210:8080/api/v1/comments/posts/1"

# 게시글의 최상위 댓글만 조회
curl "http://54.180.251.210:8080/api/v1/comments/posts/1/root"

# 특정 댓글의 대댓글 조회
curl "http://54.180.251.210:8080/api/v1/comments/1/replies"

# 댓글 상세 조회
curl "http://54.180.251.210:8080/api/v1/comments/1"
```

**댓글 수정/삭제 (인증 필요)**
```bash
# 댓글 수정
curl -X PUT "http://54.180.251.210:8080/api/v1/comments/1?currentUserId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "content": "수정된 댓글 내용입니다."
  }'

# 댓글 삭제 (소프트 삭제)
curl -X DELETE "http://54.180.251.210:8080/api/v1/comments/1?currentUserId=1" \
  -H "Authorization: Bearer $TOKEN"
```

### 9. 게시글 좋아요 테스트 (좋아요 기능)

**좋아요 토글 (인증 필요)**
```bash
# 1. 로그인하여 토큰 받기 (위 5번 참고)
TOKEN=$(curl -s -X POST http://54.180.251.210:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"Password123@"}' \
  | grep -o '"accessToken":"[^"]*' | cut -d'"' -f4)

# 2. 게시글 좋아요 (첫 번째 호출 시 좋아요 추가)
curl -X POST "http://54.180.251.210:8080/api/v1/posts/1/like?currentUserId=1" \
  -H "Authorization: Bearer $TOKEN"

# 3. 게시글 좋아요 다시 호출 (좋아요 취소)
curl -X POST "http://54.180.251.210:8080/api/v1/posts/1/like?currentUserId=1" \
  -H "Authorization: Bearer $TOKEN"
```

**응답 예시 (좋아요 추가):**
```json
{
  "success": true,
  "data": {
    "postId": 1,
    "isLiked": true,
    "totalLikeCount": 1,
    "message": "좋아요를 누르셨습니다"
  },
  "message": null,
  "timestamp": "2025-12-22T13:00:00"
}
```

**좋아요 상태 및 정보 조회**
```bash
# 좋아요 상태 확인
curl "http://54.180.251.210:8080/api/v1/posts/1/like/status?currentUserId=1"

# 게시글 좋아요 수 조회
curl "http://54.180.251.210:8080/api/v1/posts/1/like/count"

# 게시글을 좋아요한 사용자 목록 조회
curl "http://54.180.251.210:8080/api/v1/posts/1/likes"

# 내가 좋아요한 게시글 목록 조회
curl "http://54.180.251.210:8080/api/v1/posts/likes/me?currentUserId=1"
```

### 10. 게시글 스크랩 테스트 (스크랩 기능) ✨ NEW!

**스크랩 추가 (인증 필요)**
```bash
# 1. 로그인하여 토큰 받기
TOKEN=$(curl -s -X POST http://54.180.251.210:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"Password123@"}' \
  | grep -o '"accessToken":"[^"]*' | cut -d'"' -f4)

# 2. 게시글 스크랩 (기본 폴더에)
curl -X POST "http://54.180.251.210:8080/api/v1/posts/1/scrap?currentUserId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "postId": 1,
    "folderId": null
  }'

# 3. 특정 폴더에 스크랩
curl -X POST "http://54.180.251.210:8080/api/v1/posts/2/scrap?currentUserId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "postId": 2,
    "folderId": 1
  }'
```

**응답 예시 (스크랩 추가):**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "post": {
      "id": 1,
      "title": "첫 번째 게시글"
    },
    "scrapFolder": {
      "id": 1,
      "name": "기본 폴더",
      "isDefault": true
    },
    "createdAt": "2025-12-22T15:00:00"
  },
  "message": "게시글이 스크랩되었습니다",
  "timestamp": "2025-12-22T15:00:00"
}
```

**스크랩 취소 및 관리**
```bash
# 스크랩 취소
curl -X DELETE "http://54.180.251.210:8080/api/v1/posts/1/scrap?currentUserId=1" \
  -H "Authorization: Bearer $TOKEN"

# 스크랩 상태 확인
curl "http://54.180.251.210:8080/api/v1/posts/1/scrap/status?currentUserId=1"

# 게시글 스크랩 수 조회
curl "http://54.180.251.210:8080/api/v1/posts/1/scrap/count"

# 내 스크랩 목록 조회
curl "http://54.180.251.210:8080/api/v1/posts/scraps/me?currentUserId=1"

# 스크랩 폴더 이동
curl -X PUT "http://54.180.251.210:8080/api/v1/posts/1/scrap/move?currentUserId=1&targetFolderId=2" \
  -H "Authorization: Bearer $TOKEN"

# 스크랩 검색
curl "http://54.180.251.210:8080/api/v1/posts/scraps/me/search?currentUserId=1&keyword=테스트"
```

### 11. 스크랩 폴더 관리 테스트 (폴더 기능) ✨ NEW!

**스크랩 폴더 생성 (인증 필요)**
```bash
# 1. 새 스크랩 폴더 생성
curl -X POST "http://54.180.251.210:8080/api/v1/scrap-folders?currentUserId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name": "개발 자료",
    "description": "개발 관련 유용한 게시글 모음"
  }'

# 2. 다른 폴더 생성
curl -X POST "http://54.180.251.210:8080/api/v1/scrap-folders?currentUserId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name": "디자인 참고",
    "description": "UI/UX 디자인 레퍼런스"
  }'
```

**응답 예시 (폴더 생성):**
```json
{
  "success": true,
  "data": {
    "id": 2,
    "userId": 1,
    "name": "개발 자료",
    "description": "개발 관련 유용한 게시글 모음",
    "isDefault": false,
    "createdAt": "2025-12-22T15:10:00"
  },
  "message": "스크랩 폴더가 생성되었습니다",
  "timestamp": "2025-12-22T15:10:00"
}
```

**스크랩 폴더 관리**
```bash
# 내 스크랩 폴더 목록 조회
curl "http://54.180.251.210:8080/api/v1/scrap-folders/me?currentUserId=1"

# 특정 폴더 상세 조회
curl "http://54.180.251.210:8080/api/v1/scrap-folders/2?currentUserId=1"

# 폴더 수정
curl -X PUT "http://54.180.251.210:8080/api/v1/scrap-folders/2?currentUserId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name": "개발 참고자료",
    "description": "백엔드 개발 관련 자료"
  }'

# 특정 폴더의 스크랩 목록 조회
curl "http://54.180.251.210:8080/api/v1/posts/scrap-folders/2/scraps?currentUserId=1"

# 빈 폴더 목록 조회
curl "http://54.180.251.210:8080/api/v1/scrap-folders/me/empty?currentUserId=1"

# 폴더 삭제 (스크랩은 기본 폴더로 이동)
curl -X DELETE "http://54.180.251.210:8080/api/v1/scrap-folders/2?currentUserId=1&moveToDefault=true" \
  -H "Authorization: Bearer $TOKEN"

# 기본 폴더 설정
curl -X POST "http://54.180.251.210:8080/api/v1/scrap-folders/2/set-default?currentUserId=1" \
  -H "Authorization: Bearer $TOKEN"
```

### 12. 포인트 시스템 테스트 (포인트 & 레벨) ✨ NEW!

**내 포인트 정보 조회**
```bash
# 1. 로그인하여 토큰 받기
TOKEN=$(curl -s -X POST http://54.180.251.210:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"Password123@"}' \
  | grep -o '"accessToken":"[^"]*' | cut -d'"' -f4)

# 2. 내 포인트 정보 조회

curl "http://54.180.251.210:8080/api/v1/points/me?currentUserId=1" \
  -H "Authorization: Bearer $TOKEN"
```

**응답 예시:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "userId": 1,
    "totalPoints": 150,
    "availablePoints": 145,
    "currentLevel": "LEVEL_2",
    "levelDisplayName": "일반",
    "levelNumber": 2,
    "pointsToNextLevel": 350,
    "dailyEarnedPoints": 10,
    "remainingDailyLimit": 90,
    "lastEarnedDate": "2025-12-22",
    "createdAt": "2025-12-22T10:00:00"
  }
}
```

```bash
**특정 사용자 생성**
curl -X POST http://54.180.251.210:8080/api/v1/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test2@example.com",
    "password": "Password123@",
    "nickname": "testuser2"
  }'


**관리자 포인트 관리 (관리자 전용)**
```bash
# 사용자에게 포인트 지급
curl -X POST "http://54.180.251.210:8080/api/v1/points/admin/adjust?currentUserId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "targetUserId": 2,
    "points": 100,
    "reason": "이벤트 당첨 보상"
  }'

# 사용자 포인트 차감
curl -X POST "http://54.180.251.210:8080/api/v1/points/admin/adjust?currentUserId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "targetUserId": 2,
    "points": -50,
    "reason": "부적절한 게시글 작성"
  }'

```bash
# 특정 사용자 포인트 조회
curl "http://54.180.251.210:8080/api/v1/points/admin/users/1" \
  -H "Authorization: Bearer $TOKEN"

# 특정 레벨 이상 사용자 조회
curl "http://54.180.251.210:8080/api/v1/points/admin/users/level/LEVEL_5" \
  -H "Authorization: Bearer $TOKEN"
```


**포인트 거래 내역 및 랭킹**
```bash
# 내 포인트 거래 내역 조회
curl "http://54.180.251.210:8080/api/v1/points/me/transactions?currentUserId=1" \
  -H "Authorization: Bearer $TOKEN"

# 기간별 거래 내역
curl "http://54.180.251.210:8080/api/v1/points/me/transactions/period?currentUserId=1&startDate=2025-12-01T00:00:00&endDate=2025-12-31T23:59:59" \
  -H "Authorization: Bearer $TOKEN"

# 포인트 사용
curl -X POST "http://54.180.251.210:8080/api/v1/points/me/use?currentUserId=2" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "points": 50,
    "description": "프로필 배경 이미지 구매"
  }'

# 포인트 랭킹 조회 (상위 50명)
curl "http://54.180.251.210:8080/api/v1/points/ranking?size=50" \
  -H "Authorization: Bearer $TOKEN"

# 레벨별 사용자 통계
curl "http://54.180.251.210:8080/api/v1/points/statistics/levels" \
  -H "Authorization: Bearer $TOKEN"

# 전체 포인트 통계
curl "http://54.180.251.210:8080/api/v1/points/statistics/total" \
  -H "Authorization: Bearer $TOKEN"
```


### 13. 역할 및 권한 관리 테스트 (RBAC) ✨ NEW!

**역할 및 권한 정보 조회 (인증 불필요)**
```bash
# 1. 모든 역할 정보 조회
curl "http://54.180.251.210:8080/api/v1/roles" \
  -H "Authorization: Bearer $TOKEN"

# 2. 특정 역할 상세 정보 조회
curl "http://54.180.251.210:8080/api/v1/roles/MODERATOR" \
  -H "Authorization: Bearer $TOKEN"

# 3. 모든 권한 정보 조회
curl "http://54.180.251.210:8080/api/v1/roles/permissions" \
  -H "Authorization: Bearer $TOKEN"

# 4. 특정 역할의 권한 목록 조회
curl "http://54.180.251.210:8080/api/v1/roles/ADMIN/permissions" \
  -H "Authorization: Bearer $TOKEN"

# 5. 역할별 사용자 통계
curl "http://54.180.251.210:8080/api/v1/roles/statistics" \
  -H "Authorization: Bearer $TOKEN"
```

**응답 예시 (역할 정보):**
```json
{
  "success": true,
  "data": [
    {
      "role": "USER",
      "displayName": "일반 유저",
      "description": "기본 권한을 가진 일반 사용자",
      "minimumLevel": 0,
      "permissions": ["POST_READ", "POST_CREATE", "POST_UPDATE_OWN", "..."],
      "userCount": 150
    },
    {
      "role": "POWER_USER",
      "displayName": "파워 유저",
      "description": "높은 레벨의 활동적인 사용자 (레벨 7+)",
      "minimumLevel": 7,
      "permissions": ["POST_READ", "POST_CREATE", "POST_UPDATE_OWN", "..."],
      "userCount": 25
    },
    {
      "role": "MODERATOR",
      "displayName": "부관리자",
      "description": "콘텐츠 관리 및 신고 처리 권한",
      "minimumLevel": 0,
      "permissions": ["POST_UPDATE_ALL", "POST_DELETE_ALL", "REPORT_MANAGE", "..."],
      "userCount": 5
    }
  ]
}
```

**사용자 권한 확인**
```bash
# 로그인하여 토큰 받기
TOKEN=$(curl -s -X POST http://54.180.251.210:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"Password123@"}' \
  | grep -o '"accessToken":"[^"]*' | cut -d'"' -f4)

# 특정 사용자의 특정 권한 확인
curl "http://54.180.251.210:8080/api/v1/roles/check-permission?userId=2&permission=POST_DELETE_ALL" \
  -H "Authorization: Bearer $TOKEN"
```

**응답 예시 (권한 확인):**
```json
{
  "success": true,
  "data": {
    "userId": 1,
    "permission": "POST_DELETE_ALL",
    "hasPermission": false,
    "message": "권한이 없습니다"
  }
}
```

**관리자 역할 관리 (관리자 전용)**
```bash
# 1. 사용자 역할 변경 (관리자만 가능)
curl -X POST "http://54.180.251.210:8080/api/v1/roles/admin/change?currentUserId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "targetUserId": 2,
    "newRole": "MODERATOR"
  }'

# 2. 특정 역할의 사용자 목록 조회
curl "http://54.180.251.210:8080/api/v1/roles/admin/MODERATOR/users" \
  -H "Authorization: Bearer $TOKEN"

# 3. 관리자 목록 조회
curl "http://54.180.251.210:8080/api/v1/roles/admin/admins" \
  -H "Authorization: Bearer $TOKEN"
```

**응답 예시 (역할 변경 성공):**
```json
{
  "success": true,
  "data": null,
  "message": "역할이 변경되었습니다",
  "timestamp": "2025-12-22T16:00:00"
}
```

**역할 자동 승격 시나리오:**
```bash
# 시나리오: 사용자가 LEVEL 7에 도달하면 자동으로 POWER_USER로 승격
# 1. 사용자 포인트 조회 (현재 LEVEL 6, USER 역할)
curl "http://54.180.251.210:8080/api/v1/points/me?currentUserId=2" \
  -H "Authorization: Bearer $TOKEN"

# → totalPoints: 7500, currentLevel: LEVEL_6

# 2. 관리자가 포인트 지급 (LEVEL 7로 상승)
curl -X POST "http://54.180.251.210:8080/api/v1/points/admin/adjust?currentUserId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "targetUserId": 2,
    "points": 1000,
    "reason": "활동 보상"
  }'

# 3. 포인트 재조회 (LEVEL 7 달성, 자동으로 POWER_USER로 승격됨)
curl "http://54.180.251.210:8080/api/v1/points/me?currentUserId=2" \
  -H "Authorization: Bearer $TOKEN"

# → totalPoints: 8500, currentLevel: LEVEL_7, role: POWER_USER
```

### 14. 신고 시스템 테스트 (신고 관리) ✨ NEW!

**게시글 신고하기 (인증 필요)**
```bash
# 1. 로그인하여 토큰 받기
TOKEN=$(curl -s -X POST http://54.180.251.210:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"Password123@"}' \
  | grep -o '"accessToken":"[^"]*' | cut -d'"' -f4)

# 2. 게시글 신고 (스팸)
curl -X POST "http://54.180.251.210:8080/api/v1/reports?currentUserId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "targetType": "POST",
    "targetId": 1,
    "reason": "SPAM",
    "description": "스팸성 광고 게시글입니다."
  }'

# 3. 댓글 신고 (욕설)
curl -X POST "http://54.180.251.210:8080/api/v1/reports?currentUserId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "targetType": "COMMENT",
    "targetId": 10,
    "reason": "ABUSE",
    "description": "욕설이 포함된 댓글입니다."
  }'

# 4. 사용자 신고 (혐오 발언)
curl -X POST "http://54.180.251.210:8080/api/v1/reports?currentUserId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "targetType": "USER",
    "targetId": 3,
    "reason": "HATE_SPEECH",
    "description": "지속적으로 혐오 발언을 합니다."
  }'
```

**응답 예시 (신고 생성):**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "reporterId": 1,
    "reportedUserId": 2,
    "targetType": "POST",
    "targetId": 5,
    "reason": "SPAM",
    "description": "스팸성 광고 게시글입니다.",
    "status": "PENDING",
    "reviewerId": null,
    "reviewComment": null,
    "reviewedAt": null,
    "actionTaken": null,
    "createdAt": "2025-12-22T18:00:00",
    "updatedAt": "2025-12-22T18:00:00"
  },
  "message": "신고가 접수되었습니다",
  "timestamp": "2025-12-22T18:00:00"
}
```

**신고 조회 및 관리**
```bash
# 신고 상세 조회
curl "http://54.180.251.210:8080/api/v1/reports/1" \
  -H "Authorization: Bearer $TOKEN"

# 내가 신고한 목록 조회
curl "http://54.180.251.210:8080/api/v1/reports/me?currentUserId=1" \
  -H "Authorization: Bearer $TOKEN"

# 특정 게시글에 대한 신고 목록 조회
curl "http://54.180.251.210:8080/api/v1/reports/target/POST/1" \
  -H "Authorization: Bearer $TOKEN"

# 특정 댓글에 대한 신고 수 조회
curl "http://54.180.251.210:8080/api/v1/reports/statistics/target/COMMENT/1/count" \
  -H "Authorization: Bearer $TOKEN"
```

**관리자 신고 관리 (관리자 전용)**
```bash
# 1. 대기 중인 신고 목록 조회
curl "http://54.180.251.210:8080/api/v1/reports/pending?page=0&size=20" \
  -H "Authorization: Bearer $TOKEN"

# 2. 고위험 신고 목록 조회 (음란물, 폭력, 혐오발언 등)
curl "http://54.180.251.210:8080/api/v1/reports/high-severity?page=0&size=20" \
  -H "Authorization: Bearer $TOKEN"

# 3. 상태별 신고 목록 조회 (PENDING, IN_REVIEW, APPROVED, REJECTED)
curl "http://54.180.251.210:8080/api/v1/reports/status/PENDING?page=0&size=20" \
  -H "Authorization: Bearer $TOKEN"

# 4. 신고 검토 시작 (관리자 ID: 1)
curl -X POST "http://54.180.251.210:8080/api/v1/reports/1/review/start?currentUserId=1" \
  -H "Authorization: Bearer $TOKEN"

# 5. 신고 승인 및 조치
curl -X POST "http://54.180.251.210:8080/api/v1/reports/1/approve?currentUserId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "reviewComment": "스팸 게시글로 확인되어 삭제 조치했습니다.",
    "actionTaken": "게시글 삭제 + 작성자 경고"
  }'

# 6. 신고 반려
curl -X POST "http://54.180.251.210:8080/api/v1/reports/2/reject?currentUserId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "reviewComment": "신고 내용이 부적절하여 반려합니다."
  }'

# 7. 신고 통계 조회
curl "http://54.180.251.210:8080/api/v1/reports/statistics" \
  -H "Authorization: Bearer $TOKEN"

# 8. 기간별 신고 수 조회
curl "http://54.180.251.210:8080/api/v1/reports/statistics/count?startDate=2025-12-01T00:00:00&endDate=2025-12-31T23:59:59" \
  -H "Authorization: Bearer $TOKEN"

# 9. 특정 사용자에 대한 신고 목록 조회
curl "http://54.180.251.210:8080/api/v1/reports/user/3?page=0&size=20" \
  -H "Authorization: Bearer $TOKEN"

# 10. 대상 유형별 신고 목록 (POST, COMMENT, USER, CHAT)
curl "http://54.180.251.210:8080/api/v1/reports/type/POST?page=0&size=20" \
  -H "Authorization: Bearer $TOKEN"
```

**응답 예시 (신고 통계):**
```json
{
  "success": true,
  "data": {
    "totalReports": 150,
    "pendingReports": 25,
    "inReviewReports": 10,
    "approvedReports": 100,
    "rejectedReports": 15,
    "highSeverityReports": 8
  }
}
```

**신고 사유 종류 (ReportReason):**
- **SPAM**: 스팸/광고 (심각도: 10)
- **ABUSE**: 욕설/비방 (심각도: 50)
- **SEXUAL**: 음란물 (심각도: 100) - 고위험
- **VIOLENCE**: 폭력적 콘텐츠 (심각도: 80) - 고위험
- **HATE_SPEECH**: 혐오 발언 (심각도: 100) - 고위험
- **MISINFORMATION**: 허위 정보 (심각도: 30)
- **COPYRIGHT**: 저작권 침해 (심각도: 20)
- **PERSONAL_INFO**: 개인정보 노출 (심각도: 70) - 고위험
- **ILLEGAL_CONTENT**: 불법 콘텐츠 (심각도: 100) - 고위험
- **ETC**: 기타 (심각도: 5)

**신고 상태 (ReportStatus):**
- **PENDING**: 접수 (신고 접수됨, 검토 대기 중)
- **IN_REVIEW**: 검토중 (관리자가 신고 검토 중)
- **APPROVED**: 승인 (신고 승인, 조치 완료)
- **REJECTED**: 반려 (신고 반려)

**신고 대상 유형 (ReportTargetType):**
- **POST**: 게시글
- **COMMENT**: 댓글
- **CHAT**: 채팅 (미구현)
- **USER**: 사용자

### 15. 사용자 제재 시스템 테스트 (제재 & 패널티) ✨ NEW!

**제재 타입 (PenaltyType):**
- **POST_BAN_24H**: 글쓰기 금지 24시간
- **POST_BAN_7D**: 글쓰기 금지 7일
- **POST_BAN_PERMANENT**: 글쓰기 영구 금지
- **COMMENT_BAN_24H**: 댓글 금지 24시간
- **COMMENT_BAN_7D**: 댓글 금지 7일
- **COMMENT_BAN_PERMANENT**: 댓글 영구 금지
- **FULL_BAN**: 계정 정지

**수동 제재 부여 (관리자 전용)**
```bash
# 1. 관리자 로그인하여 토큰 받기
TOKEN=$(curl -s -X POST http://54.180.251.210:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","password":"Password123@"}' \
  | grep -o '"accessToken":"[^"]*' | cut -d'"' -f4)

# 2. 24시간 글쓰기 금지 제재 부여
curl -X POST "http://54.180.251.210:8080/api/v1/penalties?currentUserId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "targetUserId": 2,
    "penaltyType": "POST_BAN_24H",
    "reason": "스팸 게시글 작성"
  }'

# 3. 7일 댓글 금지 제재 부여
curl -X POST "http://54.180.251.210:8080/api/v1/penalties?currentUserId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "targetUserId": 3,
    "penaltyType": "COMMENT_BAN_7D",
    "reason": "악성 댓글 반복"
  }'

# 4. 계정 정지 (영구)
curl -X POST "http://54.180.251.210:8080/api/v1/penalties?currentUserId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "targetUserId": 4,
    "penaltyType": "FULL_BAN",
    "reason": "고위험 신고 승인 - 음란물"
  }'
```

**응답 예시 (제재 부여):**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "userId": 2,
    "penaltyType": "POST_BAN_24H",
    "penaltyTypeName": "글쓰기 금지 24시간",
    "reason": "스팸 게시글 작성",
    "startDate": "2025-12-23T10:00:00",
    "endDate": "2025-12-24T10:00:00",
    "isActive": true,
    "isPermanent": false,
    "remainingMillis": 86400000,
    "createdBy": 1,
    "createdAt": "2025-12-23T10:00:00"
  },
  "message": "제재가 부여되었습니다",
  "timestamp": "2025-12-23T10:00:00"
}
```

**제재 조회 및 관리**
```bash
# 사용자의 활성 제재 조회
curl "http://54.180.251.210:8080/api/v1/penalties/user/2/active" \
  -H "Authorization: Bearer $TOKEN"

# 사용자의 모든 제재 이력 조회
curl "http://54.180.251.210:8080/api/v1/penalties/user/2?page=0&size=20" \
  -H "Authorization: Bearer $TOKEN"

# 모든 활성 제재 목록 조회 (관리자)
curl "http://54.180.251.210:8080/api/v1/penalties/active?page=0&size=20" \
  -H "Authorization: Bearer $TOKEN"

# 제재 해제
curl -X DELETE "http://54.180.251.210:8080/api/v1/penalties/1?currentUserId=1" \
  -H "Authorization: Bearer $TOKEN"
```

**제재 확인 API**
```bash
# 글쓰기 가능 여부 확인
curl "http://54.180.251.210:8080/api/v1/penalties/users/2/can-post"

# 댓글 작성 가능 여부 확인
curl "http://54.180.251.210:8080/api/v1/penalties/users/2/can-comment"
```

**응답 예시 (제재 확인):**
```json
{
  "success": true,
  "data": {
    "userId": 2,
    "canPost": false,
    "canComment": true,
    "message": "제재 중입니다"
  }
}
```

**제재 중 글쓰기 시도 시 에러:**
```bash
# 제재 중인 사용자가 게시글 작성 시도
curl -X POST "http://54.180.251.210:8080/api/v1/posts?currentUserId=2" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "categoryId": 1,
    "title": "테스트",
    "content": "테스트 내용",
    "contentType": "MARKDOWN"
  }'

# 에러 응답
{
  "success": false,
  "message": "글쓰기 제재 중입니다. 게시글을 작성할 수 없습니다.",
  "errorCode": "USER_PENALTY_ERROR",
  "timestamp": "2025-12-23T10:05:00"
}
```

**자동 제재 시나리오:**
```bash
# 시나리오: 신고 3회 승인 → 자동 24시간 글쓰기 금지
# 1. 사용자에 대한 신고 승인 (1회)
curl -X POST "http://54.180.251.210:8080/api/v1/reports/1/approve?currentUserId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "reviewComment": "스팸 게시글 확인",
    "actionTaken": "게시글 삭제"
  }'

# 2. 동일 사용자에 대한 신고 승인 (2회)
curl -X POST "http://54.180.251.210:8080/api/v1/reports/2/approve?currentUserId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "reviewComment": "스팸 게시글 확인",
    "actionTaken": "게시글 삭제"
  }'

# 3. 동일 사용자에 대한 신고 승인 (3회) → 자동 24시간 제재
curl -X POST "http://54.180.251.210:8080/api/v1/reports/3/approve?currentUserId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "reviewComment": "스팸 게시글 확인",
    "actionTaken": "게시글 삭제"
  }'

# 4. 활성 제재 확인 → POST_BAN_24H 제재가 자동 부여됨
curl "http://54.180.251.210:8080/api/v1/penalties/user/2/active" \
  -H "Authorization: Bearer $TOKEN"
```

---

## 📝 개발 참고사항

### 패키지 구조
```
com.community.platform
├── content          # 콘텐츠 도메인 (게시글, 댓글, 카테고리, 태그)
│   ├── domain
│   ├── application
│   ├── presentation.web
│   ├── infrastructure.persistence
│   ├── dto
│   └── exception
├── user             # 사용자 도메인 
│   ├── domain
│   ├── application  # UserService, AuthService
│   ├── presentation.web  # UserController, AuthController
│   ├── infrastructure.persistence
│   ├── dto
│   └── exception
├── engagement       # 참여 도메인 (좋아요, 스크랩)
├── notification     # 알림 도메인
├── authorization    # 권한 도메인
├── reward           # 보상 도메인
├── moderation       # 중재 도메인 (신고)
└── shared           # 공통 코드
    ├── domain
    ├── security
    └── presentation
```

### 중요 설정 파일
- `build.gradle` - Gradle 빌드 설정 및 컴파일 제외 목록
- `Dockerfile` - Docker 이미지 빌드 설정
- `docker-compose.yml` - 컨테이너 실행 설정
- `application.yml` - Spring Boot 설정

### 다음에 작업할 때 참고
1. **Exception 클래스 import**: `exception` 패키지에서 import 필요
   ```java
   import com.community.platform.user.exception.UserNotFoundException;
   import com.community.platform.content.exception.PostNotFoundException;
   ```

2. **Repository import**: `infrastructure.persistence` 패키지에서 import
   ```java
   import com.community.platform.user.infrastructure.persistence.UserRepository;
   ```

3. **MapStruct 설정**: build.gradle에서 Lombok보다 먼저 처리

4. **PostRepository 특이사항**: PostRepositoryImpl 제외로 인해 PostRepositoryCustom 상속 제거됨
   - PostRepositoryImpl 복구 후 다시 연결 필요

---

## 🎯 로드맵

### ✅ Phase 1: 기본 API (완료 - 2025-12-21)
1. ✅ 회원가입/로그인 테스트 가능

### ✅ Phase 2: 핵심 기능 복구 (완료 - 2025-12-22)
1. ✅ 게시글 CRUD, 검색, 인기글 테스트 가능

### ✅ Phase 3: 댓글 기능 복구 (완료 - 2025-12-22)
1. ✅ 댓글/대댓글 CRUD API 사용 가능
2. ✅ 계층형 댓글 구조 지원
3. ✅ Redis 연동 완료 (AWS ElastiCache)

### ✅ Phase 4: 좋아요 기능 복구 (완료 - 2025-12-22)
1. ✅ 좋아요 토글, 상태 확인, 목록 조회 API 사용 가능
2. ✅ 게시글 좋아요 수 자동 업데이트
3. ✅ 좋아요한 사용자/게시글 목록 조회 기능

### ✅ Phase 5: 스크랩 기능 복구 (완료 - 2025-12-22)
1. ✅ 스크랩 추가/취소, 폴더 관리 API 사용 가능
2. ✅ 스크랩 검색, 폴더 이동, 통계 조회 기능

### ✅ Phase 6-1: 보상 시스템 - 포인트 & 레벨 (완료 - 2025-12-22)
1. ✅ 도메인 모델 설계 (UserPoint, UserLevel, PointTransaction)
2. ✅ 레벨 시스템 구현 (10단계 레벨, 포인트 구간별 분류)
3. ✅ 포인트 적립/차감/사용 기능
4. ✅ 일일 포인트 획득 한도 시스템
5. ✅ 포인트 거래 내역 추적
6. ✅ 포인트 랭킹 및 통계 기능
7. ✅ 관리자 포인트 지급/차감 기능

### ✅ Phase 6-2: RBAC - 역할 기반 접근 제어 (완료 - 2025-12-22)
1. ✅ UserRole enum 구현 (5단계 역할 시스템)
2. ✅ Permission enum 구현 (29개 세부 권한)
3. ✅ User 엔티티에 role 필드 추가
4. ✅ 레벨 기반 자동 역할 승격 (LEVEL 7+ → POWER_USER)
5. ✅ 역할 관리 서비스 및 API
6. ✅ 권한 확인 및 검증 기능
7. ✅ 관리자 역할 변경 기능
8. ⏳ Spring Security 통합 (다음 단계)

### ✅ Phase 6-3: 신고 관리 시스템 (완료 - 2025-12-22) ✨ NEW!
1. ✅ 도메인 모델 설계 (Report, ReportTargetType, ReportStatus, ReportReason)
2. ✅ 신고 생성 및 중복 체크 기능
3. ✅ 신고 대상 유형 (게시글, 댓글, 사용자, 채팅)
4. ✅ 신고 사유 및 심각도 시스템 (9가지 사유, 심각도 점수)
5. ✅ 신고 상태 관리 (접수, 검토중, 승인, 반려)
6. ✅ 고위험 신고 자동 분류 (심각도 70+ 신고)
7. ✅ 관리자 신고 검토 및 처리 기능
8. ✅ 신고 통계 및 리포트 기능
9. ✅ 신고 처리 도메인 이벤트 (생성, 검토시작, 승인, 반려)

**역할 시스템 (5단계):**
- **USER** (일반 유저): 기본 권한 (게시글/댓글 읽기, 작성, 자신의 콘텐츠 수정/삭제, 좋아요, 스크랩, 신고)
- **POWER_USER** (파워 유저): LEVEL 7+ 자동 승격, 일반 유저 권한 포함
- **MODERATOR** (부관리자): 콘텐츠 관리 권한 (모든 게시글/댓글 수정/삭제, 신고 관리, 콘텐츠 필터 관리)
- **ADMIN** (운영자): 시스템 관리 권한 (사용자 관리, 역할 변경, 포인트 관리, 공지 관리, 카테고리 관리, 통계 조회)
- **SUPER_ADMIN** (최고 관리자): 모든 권한 (시스템 전체 제어)

**권한 카테고리 (29개 세부 권한):**
- 게시글: READ, CREATE, UPDATE_OWN, DELETE_OWN, UPDATE_ALL, DELETE_ALL, PIN, RECOMMEND
- 댓글: READ, CREATE, UPDATE_OWN, DELETE_OWN, UPDATE_ALL, DELETE_ALL
- 참여: LIKE_CREATE, SCRAP_CREATE
- 신고: REPORT_CREATE, REPORT_MANAGE
- 사용자 관리: USER_MANAGE, USER_BLOCK, USER_TEMP_BLOCK
- 시스템: ROLE_ASSIGN, POINT_MANAGE, CONTENT_FILTER_MANAGE, NOTICE_MANAGE, CATEGORY_MANAGE, STATISTICS_VIEW
- 특별: ALL (모든 권한)

**레벨 시스템:**
- LEVEL_1 (새싹): 0-99 포인트
- LEVEL_2 (일반): 100-499 포인트
- LEVEL_3 (단골): 500-999 포인트
- LEVEL_4 (열성): 1,000-1,999 포인트
- LEVEL_5 (고수): 2,000-3,999 포인트
- LEVEL_6 (달인): 4,000-7,999 포인트
- LEVEL_7 (명인): 8,000-15,999 포인트
- LEVEL_8 (전설): 16,000-31,999 포인트
- LEVEL_9 (영웅): 32,000-63,999 포인트
- LEVEL_10 (신화): 64,000+ 포인트

**포인트 획득 규칙:**
- 게시글 작성: +10점
- 게시글 발행: +5점
- 댓글 작성: +3점
- 게시글 좋아요 받음: +2점
- 댓글 좋아요 받음: +1점
- 게시글 스크랩 받음: +5점
- 일일 로그인: +5점

**포인트 차감 규칙:**
- 게시글 삭제: -5점
- 댓글 삭제: -2점
- 스팸 패널티: -50점
- 신고 패널티: -100점

---