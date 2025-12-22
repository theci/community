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
│   └── Reward (보상): 포인트 시스템
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
- **댓글 관리**: CRUD, 대댓글, 계층형 구조 ✨ NEW!
- **Redis 연결**: AWS ElastiCache 연동 완료 ✨ NEW!
- 카테고리 관리 (계층형 구조)
- 태그 시스템
- 스크랩 폴더 관리 (기본 CRUD)
- Spring Security 설정

⚠️ **부분 구현** (컴파일 제외됨)
- 좋아요/스크랩 기능

🔴 **미구현**
- 알림 시스템
- 포인트/보상 시스템
- 관리자 기능
- 실시간 기능

---

## ⚠️ 현재 빌드 상태

### 컴파일에서 제외된 파일들
다음 파일들은 미완성 상태로 빌드에서 제외되었습니다:

**Controllers (표현 계층)**
```
✗ PostLikeController.java
✗ PostScrapController.java
✗ ScrapFolderController.java
```

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

#### 💬 **댓글 API** (CommentController, CommentService) ✨ NEW!
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

---

## 📝 Postman으로 테스트하기

### 1. Postman 컬렉션 구성

#### Base URL 설정
- Variable name: `baseUrl`
- Initial value: `http://54.180.251.210:8080/api/v1`

#### 회원가입 요청
```
POST {{baseUrl}}/users/register
Content-Type: application/json

{
  "email": "test@example.com",
  "password": "Password123@",
  "nickname": "testuser"
}
```

#### 로그인 요청
```
POST {{baseUrl}}/auth/login
Content-Type: application/json

{
  "email": "test@example.com",
  "password": "Password123@"
}

Tests 탭에 추가:
pm.environment.set("accessToken", pm.response.json().data.accessToken);
```

#### 인증이 필요한 요청
```
POST {{baseUrl}}/auth/logout
Authorization: Bearer {{accessToken}}
```

---

## 🔨 앞으로 구현해야 할 부분

### ✅ 완료된 부분 (2025-12-22)

1. **UserService 복구** (2025-12-21)
   - ✅ Exception import 문제 해결
   - ✅ UserNotFoundException, DuplicateUserException 추가

2. **AuthController & UserController 복구** (2025-12-21)
   - ✅ 회원가입 API 활성화
   - ✅ 로그인 API 활성화
   - ✅ 토큰 갱신/로그아웃 API 활성화

3. **비밀번호 유효성 검증 수정** (2025-12-21)
   - ✅ UserRegisterRequest 정규식 패턴 수정
   - ✅ 길이 제한 추가 (`{8,20}$` 패턴)
   - ✅ 허용 특수문자 명시 (`@$!%*?&`)

4. **Spring Security 설정 수정** (2025-12-21)
   - ✅ `/api/v1/auth/login` 공개 API로 설정
   - ✅ `/api/v1/auth/refresh`, `/api/v1/auth/validate` 공개 API로 설정

5. **게시글 관리 기능 복구** (2025-12-22) ✨ NEW!
   - ✅ PostService Exception import 추가 (PostNotFoundException, CategoryNotFoundException)
   - ✅ PostRepositoryImpl QueryDSL Tuple 변환 문제 해결
   - ✅ PostRepository에 PostRepositoryCustom 재연결
   - ✅ PostController 활성화
   - ✅ 게시글 CRUD, 검색, 발행, 인기글, 트렌딩 API 사용 가능

### 🚧 다음 구현 단계

#### Phase 1: 좋아요/스크랩 기능 복구 (다음 우선순위)

**PostLikeController, PostScrapController, ScrapFolderController 복구**
- 각 Controller의 Service 의존성 확인
- 누락된 메서드 구현
- API 엔드포인트 테스트

#### Phase 2: 고급 기능 구현

**알림 시스템, 포인트/보상 시스템, 관리자 기능 등**

### 📋 기능별 체크리스트

#### 인증/사용자 관리
- [x] 회원가입
- [x] 로그인
- [x] 토큰 갱신
- [x] 로그아웃
- [ ] 이메일 중복 확인 API
- [ ] 닉네임 중복 확인 API
- [ ] 프로필 조회
- [ ] 프로필 수정
- [ ] 비밀번호 변경

#### 게시글 관리
- [x] 게시글 목록 조회
- [x] 게시글 상세 조회
- [x] 게시글 작성
- [x] 게시글 발행 (임시저장 → 발행)
- [x] 게시글 수정
- [x] 게시글 삭제
- [x] 게시글 검색 (복합 필터)
- [x] 인기 게시글 조회
- [x] 트렌딩 게시글 조회
- [x] 카테고리별 게시글 조회
- [x] 작성자별 게시글 조회
- [x] 유사 게시글 조회
- [x] 공지사항 관리

#### 댓글 관리 ✨ NEW!
- [x] 댓글 목록 조회 (계층형 구조)
- [x] 댓글 작성
- [x] 대댓글 작성 (최대 2단계)
- [x] 댓글 수정
- [x] 댓글 삭제 (소프트 삭제)
- [x] 댓글 상세 조회
- [x] 작성자별 댓글 조회
- [x] 댓글 검색
- [x] 댓글 차단/복원 (관리자용)

#### 참여 기능
- [ ] 게시글 좋아요
- [ ] 게시글 스크랩
- [ ] 스크랩 폴더 관리

---

## 🐛 문제 해결

### Redis 연결 상태 ✨ NEW!
Redis는 AWS ElastiCache에 연결되어 있습니다:
```
Host: test-0001-001.test.mxcsbc.apn2.cache.amazonaws.com
Port: 6379
SSL: Disabled
```
Redis는 캐싱용으로 사용되며, 세션 관리 및 성능 최적화에 활용됩니다.

### 서버가 재시작을 반복하는 경우
```bash
# 로그 확인
docker-compose logs --tail=100 community-api

# PostRepository 에러인 경우
# PostRepository에서 PostRepositoryCustom 상속 제거 확인:
# public interface PostRepository extends JpaRepository<Post, Long>
```

### 비밀번호 유효성 검증 실패 (400 Bad Request)
```json
{
  "success": true,
  "data": {
    "message": "입력 값이 올바르지 않습니다",
    "fieldErrors": [
      {
        "field": "password",
        "rejectedValue": "password123",
        "message": "비밀번호는 대소문자, 숫자, 특수문자(@$!%*?&)를 포함한 8~20자여야 합니다"
      }
    ]
  }
}
```
→ 비밀번호 규칙 확인:
- 길이: 8~20자
- 대문자, 소문자, 숫자 각각 1개 이상 필수
- 특수문자 필수 (허용: `@ $ ! % * ? &`)
- **주의**: `#` 등 다른 특수문자는 사용 불가!

### 회원가입 시 중복 이메일 에러
```json
{
  "success": false,
  "message": "이미 사용중인 이메일입니다: test@example.com",
  "errorCode": "DUPLICATE_USER"
}
```
→ 다른 이메일 주소로 시도하거나, H2 콘솔에서 데이터 삭제 후 재시도

### 로그인 실패 (401 Unauthorized)
```json
{
  "success": false,
  "message": "이메일 또는 비밀번호가 일치하지 않습니다",
  "errorCode": "AUTHENTICATION_FAILED"
}
```
→ 이메일/비밀번호 확인 또는 회원가입 먼저 진행

---

## 🛑 서버 중지

```bash
# 컨테이너 중지
docker-compose down

# 이미지까지 삭제
docker-compose down --rmi local

# 데이터베이스 초기화 (H2는 메모리DB라 재시작 시 자동 초기화)
docker-compose restart community-api
```

---

## 📊 모니터링

### 컨테이너 상태 확인
```bash
docker-compose ps
```

### 로그 실시간 확인
```bash
docker-compose logs -f community-api
```

### 리소스 사용량 확인
```bash
docker stats
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
├── user             # 사용자 도메인 ✅ 복구 완료
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

## 🎯 빠른 복구 로드맵

### ✅ Phase 1: 기본 API 복구 (완료 - 2025-12-21)
1. ✅ Exception 클래스 import 문제 해결
2. ✅ UserService 복구 → AuthController, UserController 활성화
3. ✅ 회원가입/로그인 테스트 가능

### ✅ Phase 2: 핵심 기능 복구 (완료 - 2025-12-22)
1. ✅ PostService 복구 (Exception import 수정)
2. ✅ PostRepositoryImpl 복구 (QueryDSL Tuple 변환)
3. ✅ PostRepository에 PostRepositoryCustom 재연결
4. ✅ PostController 활성화
5. ✅ 게시글 CRUD, 검색, 인기글 테스트 가능

### ✅ Phase 3: 댓글 기능 복구 (완료 - 2025-12-22) ✨ NEW!
1. ✅ CommentService 복구 (Exception import 추가, 중복 클래스 제거)
2. ✅ CommentController 활성화
3. ✅ 댓글/대댓글 CRUD API 사용 가능
4. ✅ 계층형 댓글 구조 지원
5. ✅ Redis 연동 완료 (AWS ElastiCache)

### 🚧 Phase 4: 참여 기능 복구 (다음 단계)
1. [ ] 좋아요/스크랩 Controller 활성화
2. [ ] 전체 워크플로우 테스트

---

## 🎉 테스트 시나리오

### 현재 가능한 전체 워크플로우 ✨ 댓글 기능 추가!

```bash
# 1. 회원가입
curl -X POST http://54.180.251.210:8080/api/v1/users/register \
  -H "Content-Type: application/json" \
  -d '{"email":"user1@test.com","password":"Test1234@","nickname":"user1"}'

# 2. 로그인 (JWT 토큰 받기)
TOKEN=$(curl -s -X POST http://54.180.251.210:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user1@test.com","password":"Test1234@"}' \
  | grep -o '"accessToken":"[^"]*' | cut -d'"' -f4)

# 3. 카테고리 생성 (관리자용)
curl -X POST "http://54.180.251.210:8080/api/v1/categories?name=General&description=General%20discussion%20board"

# 4. 게시글 작성
curl -X POST "http://54.180.251.210:8080/api/v1/posts?currentUserId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "categoryId": 1,
    "title": "커뮤니티 플랫폼 테스트",
    "content": "DDD 기반 커뮤니티 플랫폼 API 테스트입니다!",
    "contentType": "MARKDOWN",
    "tags": ["테스트", "DDD", "SpringBoot"]
  }'

# 5. 게시글 발행
curl -X POST "http://54.180.251.210:8080/api/v1/posts/1/publish?currentUserId=1" \
  -H "Authorization: Bearer $TOKEN"

# 6. 게시글 목록 조회 (인증 불필요)
curl "http://54.180.251.210:8080/api/v1/posts"

# 7. 게시글 상세 조회 (조회수 증가)
curl "http://54.180.251.210:8080/api/v1/posts/1"

# 8. 댓글 작성 ✨ NEW!
curl -X POST "http://54.180.251.210:8080/api/v1/comments?currentUserId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "postId": 1,
    "content": "좋은 게시글이네요!"
  }'

# 9. 대댓글 작성 ✨ NEW!
curl -X POST "http://54.180.251.210:8080/api/v1/comments?currentUserId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "postId": 1,
    "parentCommentId": 1,
    "content": "저도 동감합니다!"
  }'

# 10. 댓글 목록 조회 (계층형) ✨ NEW!
curl "http://54.180.251.210:8080/api/v1/comments/posts/1"

# 11. 인기 게시글 조회
curl "http://54.180.251.210:8080/api/v1/posts/popular?days=7"

# 12. 카테고리 조회
curl http://54.180.251.210:8080/api/v1/categories/tree

# 13. 로그아웃
curl -X POST http://54.180.251.210:8080/api/v1/auth/logout \
  -H "Authorization: Bearer $TOKEN"
```

---

**Happy Coding! 🚀**

**최종 업데이트**: 2025-12-22
- ✅ **댓글 관리 기능 완전 복구** (CommentService, CommentController) ✨ NEW!
- ✅ **Redis 연동 완료** (AWS ElastiCache 연결) ✨ NEW!
- ✅ 댓글/대댓글 CRUD, 계층형 구조, 검색 API 사용 가능
- ✅ 게시글 CRUD, 검색, 발행, 인기글, 트렌딩 API 사용 가능
- ✅ QueryDSL 기반 복합 검색 및 동적 쿼리 정상 작동
- ✅ 회원가입/로그인 → 게시글 작성 → 댓글 작성 전체 워크플로우 테스트 가능
- ✅ JWT 토큰 기반 인증 시스템 작동 확인
