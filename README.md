<p align="left">
  <a href="#"><img alt="Android OS" src="https://img.shields.io/badge/OS-Android-3DDC84?style=flat-square&logo=android"></a>
  <a href="#"><img alt="Languages-Kotlin" src="https://flat.badgen.net/badge/Language/Kotlin?icon=https://raw.githubusercontent.com/binaryshrey/Awesome-Android-Open-Source-Projects/master/assets/Kotlin_Logo_icon_white.svg&color=f18e33"/></a>
  <a href="#"><img alt="PRs" src="https://img.shields.io/badge/PRs-Welcome-3DDC84?style=flat-square"></a>
</p>

<p align="left">
:eyeglasses: The MusinsaGlobal Test contributor, Lukoh().
</p><br>

# MusinsaGlobal

MusinsaGlobal 앱은 [AdvancedAppArchitecture](https://github.com/Lukoh/AdvancedAppArchitecture) 및 [LukohSplash](https://github.com/Lukoh/LukohSplash) 및 Android 최신 아키텍처 구성 요소인 Jetpack을 기반으로 하며 MVVM 디자인 패턴을 따르고 있습니다. 또한 MusinsaGlobal 앱 아키텍처는 프리젠테이션 계층, 도메인 계층 및  계층으로 구성되어 있습니다. 그리고 MusinsaGlobal 앱에는 Advanced Android App Architecture로 새로운 Android App 개발을 위한 최신 기술이 적용되었습니다. 이러한 새로운 기술들이 안드로이드 앱이 확장되어 더욱 경쟁력 있고 일관성을 유지하도록 도와줍니다. 또한 MusinsaGlobal App 의 모든 모듈에 Kotlin 언어를 적용하고 있으며 대부분의 코드가 Kotlin으로 작성되어 있습니다. 이 MusinsaGlobal 앱 개발하기 위해서 사용된 기술스택은 아래와 같습니다:

**1. [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)** 

**2. [App Architecture(MVVM Design)](https://developer.android.com/topic/architecture)**

**3. [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)**

**4. [Dagger2[Dependency Injection]](https://developer.android.com/training/dependency-injection/dagger-basics)**

**5. [Navigation](https://developer.android.com/guide/navigation/navigation-getting-started)**

**6. [ViewBinding](https://developer.android.com/topic/libraries/view-binding)**

**7. [Kotlin Couroutine](https://developer.android.com/kotlin/coroutines)**

**8. [Kotlin Flow(HotFlow & ColdFlow)](https://developer.android.com/kotlin/flow)**

**9. [Retrofit Library](https://square.github.io/retrofit/)**

**10. [OkHttp](https://square.github.io/okhttp/)**

**11. [Coil Image Loading Library](https://coil-kt.github.io/coil/)**

**12. [Gson](https://github.com/google/gson)**

**13. [Chrome Custom Tabs](https://developer.chrome.com/docs/android/custom-tabs/)**

Jetpack 라이브러러인 Compose 학습을 계획하고 있습니다. (2월 초에 현재 [AdvancedAppArchitecture](https://github.com/Lukoh/AdvancedAppArchitecture) 의 데모앱인 [Weather](https://github.com/Lukoh/Weather)에 적용 시작할 예정입니다.)


MusinsaGlobal 앱은 [AdvancedAppArchitecture](https://github.com/Lukoh/AdvancedAppArchitecture) Architecture 기반으로 개발된 Android 앱입니다. 또한 Clean Architecture & Clean Code 기반으로 개발되어 있고 앱을 확장하거나 유지보수 용이하고, 개발 생산성을 높일수 있는 구조를 가지고 있습니다. [AdvancedAppArchitecture](https://github.com/Lukoh/AdvancedAppArchitecture) 기반으로 앱을 개발하면 비지니스 로직과 UI Layout 만 새로 적용하면 여러용도의 서로 다른 앱을 쉽게 안정성 있게, 그리고 빠르게 개발을 할수 았습니다. ([AdvancedAppArchitecture](https://github.com/Lukoh/AdvancedAppArchitecture) Architecture 는 ShareableAsset 앱 과 OHeadline 앱 서비스 개발에도 적용된 Architecture 로써 코드 생산성 & 유지보수 & 확장성 & 호율성에 강점을 가진 Android 앱을 개발하긴 위한 좋은 App Architecture 입니다.)

아래 내용들은 제가 제 [Mediem](https://medium.com/@lukohnam) 에 올린 좋은 아키텍쳐 기반에서 앱을 개발 하는 방법들에 관한 기술글들입니다. 약 3년전 개발된 AppArchitecture 를 기반으로 LukohSplash 를 개발하였고, MusinsaGlobal 앱은 LukohSplash 를 좀더 개선한 AdvancedAppArchitecture 를 기반으로 개발되었습니다. 제 기술 스택을 참고하시려면 [LukohSplahs](https://github.com/Lukoh/LukohSplash) 와 [AdvancedAppArchitecture](https://github.com/Lukoh/AdvancedAppArchitecture) 를 참고해 주십시요.

Clean Architecture를 기반으로하는 MVVM 디자인은 코드 기반의 책임을 분리하는 데 한 단계 더 나아갈 수 있고, 앱에서 수행할 수 있는 작업의 논리를 명확하게 추상화 할 수 있습니다.

Clean Architecture 의 핵심 기본 원칙은 아래와 같습니다:

**1. Separation of concerns (SoC):** 
   
    It is a design principle for separating a computer program into distinct sections such that each section addresses a separate concern. 
    A concern is anything that matters in providing a solution to a problem.
   
    ❏ This principle is closely related to the Single Responsibility Principle of object-oriented programming which states that “every module, class, 
    or function should have responsibility over a single part of the functionality provided by the software, and that responsibility should be entirely
    encapsulated by the class, module or function.”
						
**2. Drive UI from a model:** 
    
    App should drive the UI from a model, preferably a persistent model. Models are independent from the View objects and app components, 
    so they're unaffected by the app's life-cycle and the associated concerns. Business logic is completely separated from UI. It makes our code very easy to 
    maintain and test.It makes all code very easy to maintain and test.
    
**Clean Architecture 장점**
 * 일반 MVVM 보다 훨씬 쉽게 테스트할 수 있습니다.
 * 코드가 더욱 분리됩니다. (가장 큰 이점)
 * 패키지 구조는 탐색하기가 훨씬 더 쉽습니다.
 * 유지 관리가 훨씬 더 쉽습니다.
 * 새로운 기능을 훨씬 더 빠르게 추가할 수 있습니다.
 
**Clean Architecture 단점**
 * 약간 가파른 학습 곡선이 있습니다. 모든 레이어가 함께 작동하는 방식을 이해하는 데 시간이 걸릴 수 있습니다. 특히 간단한 MVVM 또는 MVP와 같은 패턴에서 오는 경우 더욱 그렇습니다..
 * 추가 클래스들을 추가하므로 복잡성이 낮은 프로젝트에는 적합하지 않습니다. 

MusinsaGlobal 앱의 Architecture는 Presentation Layer & Domain(Business Logic) Layer & Data Laery의 3개 Layer로 구성되어 있습니다.

**1. The presentation layer**
 
프리젠테이션 계층은 사용자 계층으로, 사용자의 이벤트를 캡처하고 결과를 표시하는 그래픽 인터페이스입니다. 또한 사용자가 입력한 데이터에 서식 오류가 없는지 확인하고 데이터를 특정 방식으로 표시되도록 서식을 지정하는 등의 작업을 수행합니다. 이 데모 앱에서 이러한 작업은 UI 레이어와 ViewModel 레이어 간에 공유됩니다.:
 * UI 계층에는 사용자 이벤트를 캡처하고 데이터를 표시하는 활동 및 프래그먼트가 포함되어 있습니다.
 * ViewModel 계층은 UI가 특정 방식으로 데이터를 표시하고 사용자 항목의 형식이 올바른지 확인하도록 데이터 형식을 지정합니다.

**2. The business logic layer**

이 계층에서 비즈니스가 준수해야 하는 모든 규칙은 비즈니스입니다. 이를 위해 사용자가 제공하는 데이터를 수신하고 필요한 작업을 수행합니다.
애플리케이션의 비즈니스 로직을 포함하고 비즈니스 로직에서 필요한 Model 과 UseCase를 포함하고 있습니다.
UseCase 클래스는 수행할 작업에 대한 비즈니스 규칙이고, 이것은 가장 안정적인 계층이며 개발된 소프트웨어 아키텍처에서 일어나는 일을 나타내는 계층입니다. 또한 각 개별 기능 또는 비즈니스 논리 단위라고 보시면 됩니다. 그래서 UseCase는 보통 한 개의 행동을 담당하는것이 좋은 구조입니다. (복잡해지면 문제를 찾아 내기 힘들고 수정이 힘들지만, 단순화 되면 쉽게 문제를 찾고 수정 할 수 있습니다.)

**3. The data layer**

이 계층에는 데이터가 있고 액세스할 수 있는 위치가 있습니다. 이러한 작업은 리포지토리 계층과 데이터 소스 간에 구분됩니다.

저장소 계층은 데이터 액세스 논리를 수행하는 계층입니다. 당신의 책임은 그것들을 얻고 그것들이 어디에 있는지 확인하고 매 순간 어디를 볼지 결정하는 것입니다. 예를 들어 먼저 데이터베이스를 확인하고 그렇지 않은 경우 웹에서 검색하여 로컬 데이터베이스에 저장하고 이제 저장된 데이터를 반환할 수 있습니다. 즉, 데이터에 대한 액세스 흐름을 정의합니다. 이 예에서는 API와 통신하는 데이터 계층에 직접 맥주를 요청합니다.데이터 소스 계층은 구현이 데이터에 액세스하기 위해 수행하는 것입니다:

![alt Layer Communication](https://raw.githubusercontent.com/Lukoh/LukohSplash/main/Layer%20Communication.jpeg)
			 	 	 								
**Android의 클린 아키텍처 레이어 간 통신**

각 레이어는 다을 레이어와만 켜뮤니케이션 할수 있습니다.  Software architecture scheme 는 다음과 같습니다:
 * **UI는 ViewModel과만 커뮤니케이션 할 수 있습니다.**
 * **ViewModel은 UseCase와만 커뮤니케이션 할 수 있습니다.**
 * **UseCase는 저장소와만 커뮤니케이션 할 수 있습니다.**
 * **Reposity는 데이터 소스(Paging 일 경우는 [PagingSource](https://developer.android.com/reference/kotlin/androidx/paging/PagingSource) & [PagingDataMediator](https://github.com/Lukoh/AdvancedAppArchitecture/blob/main/app/src/main/java/com/goforer/advancedapparchitecture/data/source/network/mediator/PagingDataMediator.kt) 이고, Paging 이 아닐경우 [Repository]() & [DataMediator](https://github.com/Lukoh/AdvancedAppArchitecture/blob/main/app/src/main/java/com/goforer/advancedapparchitecture/data/source/network/mediator/DataMediator.kt))와만 커뮤니케이션 할 수 있습니다.**
 
각 영역은 바로 다음 레에어와 커뮤니케이션 하며 다른 레이와와는 커뮤니케이션 하지 않습니다.

![alt App Architecture](https://github.com/Lukoh/AdvancedAppArchitecture/blob/main/flow.png) 


모듈 패턴 적용하면 비즈니스 논리에 집중하고 모듈을 단순화하고 상용구 코드를 줄이고 Module-Pattern(제가 주창한([Medium 글 참조](https://medium.com/@lukohnam/how-to-focus-effective-business-logic-implement-more-expandable-simplify-modules-reduce-the-81ae1af23e4e)) 모듈 패턴이라고 함)을 사용하는 효과적인 방법입니다. Module-Pattern(즉, 잘 설계된 앱 아키텍처)을 사용하여 잘 디자인/구현된 모바일 앱은 새로운 UI 레이아웃 및 비즈니스 로직을 적용/적용하여 새로운 앱으로 더 빠르고 쉽게 확장할 수 있습니다. 비즈니스 논리에 집중하고 더 나은 서비스를 만드는 데 도움이 되며 사용자에게 친숙한 기능을 신속하게 제공할 수 있습니다.

그리고 각 Fragment 들 사이의 데이터를 공유하거나 전달 하는 용도로 내부 [EventBus](https://github.com/Lukoh/AdvancedAppArchitecture/blob/main/app/src/main/java/com/goforer/advancedapparchitecture/presentation/event/EventBus.kt) 를 개발하여 사용했습니다. (각 Fragment 들간의 커뮤니케이션 & 파이프 라인 역할 수행) 

아래 내용들은 제가 제 [Mediem](https://medium.com/@lukohnam) 에 올린 좋은 아키텍쳐 기반에서 앱을 개발 하는 방법들에 관한 기술글들입니다. 약 3년전 개발된 AppArchitecture 를 기반으로 LukohSplash 를 개발하였고, MusinsaGlobal 앱은 LukohSplash 를 좀더 개선한 AdvancedAppArchitecture 를 기반으로 개발되었습니다. 제 기술 스택을 참고하시려면 [LukohSplahs](https://github.com/Lukoh/LukohSplash) 와 [AdvancedAppArchitecture](https://github.com/Lukoh/AdvancedAppArchitecture) 를 참고해 주십시요.
제가 [Medium](https://medium.com/@lukohnam) 에 올린 기술관련 글들과 관련 된 내용입니다. 혹시 관심을 가지고 있으시면 방문하셔서 봐주시면 감사드리겠습니다.

Please refer to [LukohSplash](https://github.com/Lukoh/LukohSplash) & [Better Android Apps Using latest advanced Architecture](https://medium.com/oheadline/better-android-apps-using-mvvm-with-clean-architecture-2cc49e68f41d) & [How to focus effective business logic & implement more expandable & simplify modules & reduce the boiler-plate code using the Module-Pattern to make Android App.](https://medium.com/@lukohnam/how-to-focus-effective-business-logic-implement-more-expandable-simplify-modules-reduce-the-81ae1af23e4e)

Please refer to [How to share/communicate events (independent data) across app components such as Activities, Fragments, Services, etc using ViewModel & SharedFlow.](https://medium.com/@lukohnam/how-to-share-communicate-events-independent-data-across-app-components-such-as-activities-353c96e32775)

Typically, the UI layer contains UI-related state and UI business logic. UI business logic is what gives your app value and the role of the UI is to display the application data on the screen and also to serve as the primary point of user interaction. Separating UI business logic from UI simplifies relationships and allows business logic to be reproduced outside of the UI for unit-testing.

Please refer to [How to handle your internal business logic using ViewModel and UseCase to decouple the logic from UI module.](https://medium.com/@lukohnam/how-to-handle-your-internal-business-logic-using-viewmodel-and-usecase-to-decouple-the-logic-from-f20ee9f7e4a5)

Please refer to [How to provide the custom back navigation handling behavior on Android 12 and higher.](https://medium.com/@lukohnam/how-to-provide-the-custom-navigation-handling-behavior-on-android-12-and-higher-62080cbaa9b6)

Now let’s dive into my open-source project, AdvancedAppArchitecture, which is based on LukohSplash & the Android MVVM with Clean Architecture and the latest libraries like Jetpack.
And I'm learning Jetpack Compose and will apply it to AdvancedAppArchitecture.

I'm happy to introdce the aritle, "Adding a domain layer", to add a Domain Layer. D It's very usefult to make Domain Layer in your project. [Adding a domain layer](https://medium.com/@donturner/adding-a-domain-layer-bc5a708a96da)

제게 이메일 주소는 아래와 같습니다.

lukoh.nam@gmail.com

