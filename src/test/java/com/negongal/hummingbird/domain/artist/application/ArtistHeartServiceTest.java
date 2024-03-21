package com.negongal.hummingbird.domain.artist.application;

// @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
// @SpringBootTest(properties = "spring.profiles.active=test")
// class ArtistHeartServiceTest {
//     @Autowired
//     private ArtistHeartService artistHeartService;
//     @Autowired
//     private ArtistHeartRepository artistHeartRepository;
//     @Autowired
//     private ArtistRepository artistRepository;
//     @Autowired
//     private UserRepository userRepository;
//     private static MockedStatic<SecurityUtil> mockedSecurityUtil;
//     User user;
//
//     @BeforeEach
//     void setUp() {
//         Artist artist = Artist.builder()
//                 .id("159125")
//                 .name("artist1")
//                 .build();
//         artistRepository.save(artist);
//         user = User.builder()
//                 .oauth2Id("test")
//                 .provider("test")
//                 .role(Role.USER)
//                 .status(UserStatus.ACTIVE)
//                 .build();
//         userRepository.save(user);
//     }
//
//     @BeforeAll
//     static void setUp2() {
//         mockedSecurityUtil = mockStatic(SecurityUtil.class);
//     }
//
//     @DisplayName("로그인 하지 않고 아티스트 좋아요 등록")
//     @Transactional
//     @Order(1)
//     @Test
//     void notLoginLikeArtistTest() {
//         // Given
//         String artistId = "159125";
//         BDDMockito.given(SecurityUtil.getCurrentUserId()).willReturn(Optional.empty());
//
//         Throwable notExistException = assertThrows(NotExistException.class, () ->
//                 artistHeartService.save(artistId));
//         assertEquals("USER_NOT_EXIST", notExistException.getMessage());
//     }
//
//     @DisplayName("존재하지 않는 아티스트 좋아요 등록")
//     @Transactional
//     @Order(2)
//     @Test
//     void NotExistArtistLikeTest() {
//         // Given
//         String notExistArtistId = "159123";
//         BDDMockito.given(SecurityUtil.getCurrentUserId()).willReturn(Optional.of(1L));
//
//         Throwable notExistException = assertThrows(NotExistException.class, () ->
//                 artistHeartService.save(notExistArtistId));
//         assertEquals("ARTIST_NOT_EXIST", notExistException.getMessage());
//     }
//
//     @DisplayName("아티스트 좋아요 등록")
//     @Transactional
//     @Order(3)
//     @Test
//     void artistLikeTest() {
//         // Given
//         String artistId = "159125";
//         Long userId = userRepository.findById(user.getId()).get().getId();
//
//         BDDMockito.given(SecurityUtil.getCurrentUserId()).willReturn(Optional.of(userId));
//
//         // When
//         artistHeartService.save(artistId);
//         ArtistHeart artistHeart = artistHeartRepository.findByUserIdAndArtistId(userId, artistId).get();
//
//         // Then
//         assertEquals(artistHeart.getArtist().getId(), artistId);
//         assertEquals(artistHeart.getUser().getId(), userId);
//     }
//
//     @DisplayName("로그인 하지 않고 아티스트 좋아요 삭제 시도")
//     @Transactional
//     @Order(4)
//     @Test
//     void notLoginDislikeArtistTest() {
//         // Given
//         String artistId = "159125";
//         // mockedSecurityUtil = mockStatic(SecurityUtil.class);
//         BDDMockito.given(SecurityUtil.getCurrentUserId()).willReturn(Optional.empty());
//
//         Throwable notExistException = assertThrows(NotExistException.class, () ->
//                 artistHeartService.delete(artistId));
//         assertEquals("USER_NOT_EXIST", notExistException.getMessage());
//     }
//
//     @DisplayName("좋아하지 않는 아티스트 좋아요 삭제 시도")
//     @Transactional
//     @Order(5)
//     @Test
//     void notLikeArtistDislikeTest() {
//         // Given
//         String artistId = "159125";
//         Long userId = 1L;
//         BDDMockito.given(SecurityUtil.getCurrentUserId()).willReturn(Optional.of(userId));
//
//         Throwable notExistException = assertThrows(NotExistException.class, () ->
//                 artistHeartService.delete(artistId));
//         assertEquals("ARTIST_NOT_LIKE", notExistException.getMessage());
//     }
//
//     @DisplayName("좋아하는 아티스트 취소")
//     @Transactional
//     @Order(6)
//     @Test
//     void dislikeLikeArtistTest() {
//         // Given
//         String artistId = "159125";
//         Long userId = userRepository.findById(user.getId()).get().getId();
//         BDDMockito.given(SecurityUtil.getCurrentUserId()).willReturn(Optional.of(userId));
//         artistHeartService.save(artistId);
//
//         // When
//         artistHeartService.delete(artistId);
//
//         // Then
//         Optional<ArtistHeart> findArtistHeart = artistHeartRepository.findByUserIdAndArtistId(userId, artistId);
//         assertEquals(Optional.empty(), findArtistHeart);
//     }
// }
