# payhere_assignment
-------------------------

## API 명세서
![Entity Relationship Diagram for Enrollment System](https://user-images.githubusercontent.com/83135775/188639049-cfd2654d-90c8-41c1-9adb-2dd06aa39940.jpg)

## 과제 관련
#### 우선 로그인 / 회원가입은 SpringBoot Security / JWT 토큰을 사용했습니다.
#### 가계부를 작성하는 CRUD를 구성했고, 해당 게시글을 조회할 때는 @AuthenticationPrincipal을 이용해 자신이 쓴 게시글만 조회되도록 구현했습니다.
#### 마찬가지로 수정 / 삭제 시 로그인 정보를 확인 후 본인만 수정 및 삭제를 할 수 있도록 구현했습니다.
