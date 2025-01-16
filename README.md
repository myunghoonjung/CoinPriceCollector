# CoinPriceCollector

## 1. 프로젝트 구조

### Java 소스
- `ApplicationRunner.java`: 애플리케이션 실행 엔트리포인트.  
- `PriceCollectorThread.java`: 가격 수집 쓰레드.  
- `PriceServiceImpl.java`: 가격 수집 서비스 구현체.  
- `PriceCollectorManager.java`: 멀티쓰레드 처리 관리.  
- `ExchangeApi.java`: 거래소 API 인터페이스.  
- `BithumbApi.java`: 빗썸 API 구현체.  
- `BinanceApi.java`: 바이낸스 API 구현체.  
- `CurrencyConverter.java`: 환율 변환 유틸리티.  

### 리소스 파일
- `price.xml`: 가격 관련 SQL 쿼리.  
- `application.yml`: Spring 설정 파일.  

---

## 2. 가격 수집 구조

1. **ApplicationRunner**: 애플리케이션 실행 시 쓰레드를 시작합니다.  
2. **PriceCollectorThread**: 무한 루프를 통해 주기적으로 가격을 수집합니다.  
3. **PriceServiceImpl**: 각 거래소에서 가격을 수집하고 평균가를 계산합니다.  
4. **BithumbApi, BinanceApi**: 각각 빗썸과 바이낸스에서 가격 데이터를 호출합니다.  
5. **CurrencyConverter**: 바이낸스의 USDT 가격을 KRW로 변환합니다.  

---

## 3. 테이블 설계 가이드

### 3.1 주요 테이블

#### 1. `price` (가격 데이터 저장)
- **역할**: 거래소-코인 페어별 실시간 가격 및 거래 데이터를 저장.  
- **컬럼**:  
  - `id_price`: 가격 데이터 고유 ID.  
  - `id_ex_pair`: 거래소-코인 페어 ID (`exchange_pair`).  
  - `price`: 현재 가격 (소수점 8자리).  
  - `quantity`: 거래량.  
  - `reg_dttm_sin`: 등록 시각 (싱가포르 시간).  
  - `reg_dttm_kor`: 등록 시각 (한국 시간).  
  - `reg_dttm_unix`: Unix Timestamp.  

#### 2. `exchange` (거래소 정보)
- **역할**: 거래소 기본 정보를 저장.  
- **컬럼**:  
  - `id_ex`: 거래소 ID.  
  - `name`: 거래소 이름.  
  - `url`: 거래소 API URL.  
  - `status`: 거래소 활성화 여부 (1: 활성, 0: 비활성).  

#### 3. `coin` (코인 정보)
- **역할**: 코인의 기본 정보를 저장.  
- **컬럼**:  
  - `id_coin`: 코인 ID.  
  - `symbol`: 코인 심볼 (예: BTC, ETH).  
  - `name`: 코인 이름.  

#### 4. `exchange_pair` (거래소-코인 페어 매핑)
- **역할**: 거래소와 코인 페어 정보를 매핑.  
- **컬럼**:  
  - `id_ex_pair`: 거래소-코인 페어 ID.  
  - `id_ex`: 거래소 ID (참조: `exchange`).  
  - `id_coin`: 코인 ID (참조: `coin`).  
  - `pair_name`: 페어 이름 (예: BTC/KRW).  

#### 5. `price_history` (가격 기록 테이블)
- **역할**: 과거 가격 데이터를 저장하여 추세 분석 가능.  
- **컬럼**:  
  - `id_price`: 가격 데이터 고유 ID.  
  - `id_ex_pair`: 거래소-코인 페어 ID (참조: `exchange_pair`).  
  - `price`: 과거 가격 (소수점 8자리).  
  - `quantity`: 거래량.  
  - `timestamp`: 가격 기록 시간 (Unix Time).  

#### 6. `currency_rate` (환율 정보 테이블)
- **역할**: USD → KRW 등의 환율 정보를 관리.  
- **컬럼**:  
  - `id_rate`: 환율 데이터 고유 ID.  
  - `base_currency`: 기준 통화 (예: USD).  
  - `target_currency`: 대상 통화 (예: KRW).  
  - `rate`: 환율 값 (소수점 8자리).  
  - `last_updated`: 환율 갱신 시간.  

#### 8. `schedule_task` (스케줄 태스크 관리 테이블)
- **역할**: 가격 수집 작업 스케줄 정보를 관리.  
- **컬럼**:  
  - `id_task`: 태스크 ID (Primary Key).  
  - `task_name`: 태스크 이름 (예: "BTC 가격 수집").  
  - `status`: 태스크 상태 (1: 활성, 0: 비활성).  
  - `last_run_time`: 마지막 실행 시간.  
  - `next_run_time`: 다음 실행 시간.  
