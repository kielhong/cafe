module.exports = `
<div id="header">
    <div class="snb_area" >
      <div class="snb_inner">
        <h1>
          <a href="http://www.naver.com" class="N=a:STA.naverlogo logo_naver">NAVER</a>
          <a href="/" class="N=a:STA.cafe logo_cafe">카페</a>
        </h1>
        <div class="blind"><a href="#start">본문 바로가기</a></div>
        <form id="frmSearch" name="frmSearch" action="/CombinationSearch.nhn" method="get" class="_submit(CafeSection|CafeSearch|query)">
          <fieldset>
            <legend class="blind">검색</legend>
            <div class="snb_search_box" role="search">
              <div class="snb_search_bsub">
                <div id="searchOptionBox" class="search_option_box off">
                  <div class="search_option">
                    <a href="#" id="displaySearchLabel" class="_click(CafeSection|ToggleSearchType)" role="button" aria-label="검색옵션선택" aria-expanded="false">
                      전체
                      <span class="ic_down"></span><span class="ic_and"></span>
                    </a>
                  </div>
                  <ul class="search_option_layer" id="searchOptionLayer">
                    <li class="N=a:STA.scall _click(CafeSection|SelectSearchType|all) _SearchOptionNode"><a href="#">전체</a></li>
                    <li class="N=a:STA.sccafe _click(CafeSection|SelectSearchType|cafe) _SearchOptionNode"><a href="#">카페</a></li>
                    <li class="N=a:STA.scarticle _click(CafeSection|SelectSearchType|article) _SearchOptionNode"><a href="#">카페글</a></li>
                    <li class="N=a:STA.scmanager _click(CafeSection|SelectSearchType|manager) _SearchOptionNode"><a href="#">매니저</a></li>
                  </ul>
                </div>
                <input type="text" title="검색" name="query" value="" accesskey="s" class="snb_search_text" maxlength="255" id="query" />
                <input type="hidden" name="where" />
                <div id="nautocomplete" style="display:inline-block;">
                  <a id="autoCompleteToggle" href="#" class="btn_arrow _stopDefault on">
                    <span class="blind">자동완성 펼치기</span>
                    <span id="autoCompleteBtn" class="ic_down" style="display:"></span>
                  </a>
                </div>
                <div style="position: absolute; left: 2px; top: 47px; z-index: 100;">
                  <iframe scrolling="no" height="0" frameborder="0" width="317" style="display:none;" marginheight="0" marginwidth="0" src="" title="자동완성" id="autoFrame"></iframe>
                </div>
              </div>
              <input alt="검색" type="submit" class="btn_search N=a:STA.search" value="검색" />
              <a href="http://search.naver.com/search.naver" target="_blank" class="btn_search_all _click(CafeSection|Nexearch)">통합검색</a>
            </div>
          </fieldset>
        </form>

        <div class="cafe_gnb_wrap">
          <div id="gnb">
            <script charset="utf-8" type="text/javascript" src="http://static.gn.naver.net/template/gnb_utf8.nhn?2017030605"></script>
          </div>
        </div>
      </div>
    </div>

    <div id="gnbMenu" class="menu">
      <div class="menu_inner" role="navigation">
        <ul>
          <li class="lnb1 ">
            <a href="http://section.cafe.naver.com" class="N=a:LNB.home"><span class="tx">카페홈</span></a>
          </li>
          <li class="lnb2 ">
            <a href="/SectionCafeStoryList.nhn" class="N=a:LNB.sto"><span class="tx">카페스토리</span></a>
          </li>
          <li class="lnb3 ">
            <a href="/RandomPowerCafeList.nhn" class="N=a:LNB.top"><span class="tx">대표카페</span></a>
          </li>
          <li class="lnb4 ">
            <a href="/CafeRankingList.nhn" class="N=a:LNB.rnk"><span class="tx">랭킹</span></a>
          </li>
          <li class="lnb5 ">
            <a href="/StarCafeList.nhn" class="N=a:LNB.star"><span class="tx">인기팬카페</span></a>
          </li>
          <li class="lnb6 ">
            <a href="/GameCafeList.nhn" class="N=a:LNB.game"><span class="tx">공식게임카페</span></a>
          </li>
          <li class="lnb7 ">
            <a href="/EducationCafeList.nhn" class="N=a:LNB.wiu"><span class="tx">WIU지식나눔카페</span></a>
          </li>
          <li class="lnb8">
            <a href="http://m.cafe.naver.com/CafeSupport.nhn" class="N=a:LNB.spt"><span class="tx">카페지원센터</span></a>
          </li>
        </ul>
      </div>
    </div>
  </div>
`;