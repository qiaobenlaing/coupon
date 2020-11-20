





<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">



  <link crossorigin="anonymous" href="https://assets-cdn.github.com/assets/frameworks-67bdbbece8dab4e0407b1dc8edc4fc44c6ddd91aaf7a7dd69cac694b4cb761c4.css" integrity="sha256-Z7277OjatOBAex3I7cT8RMbd2Rqven3WnKxpS0y3YcQ=" media="all" rel="stylesheet" />
  <link crossorigin="anonymous" href="https://assets-cdn.github.com/assets/github-82d26a01555805dfcbf0b92a2fed02dd7d26540fd1f58ec3c6f8a8e921600e1e.css" integrity="sha256-gtJqAVVYBd/L8LkqL+0C3X0mVA/R9Y7Dxvio6SFgDh4=" media="all" rel="stylesheet" />
  
  
  <link crossorigin="anonymous" href="https://assets-cdn.github.com/assets/site-f4fa6ace91e5f0fabb47e8405e5ecf6a9815949cd3958338f6578e626cd443d7.css" integrity="sha256-9PpqzpHl8Pq7R+hAXl7PapgVlJzTlYM49leOYmzUQ9c=" media="all" rel="stylesheet" />
  

  <meta name="viewport" content="width=device-width">
  
  <title>AES-of-JavaScript/aes_2.js at master · hellobajie/AES-of-JavaScript · GitHub</title>
  <link rel="search" type="application/opensearchdescription+xml" href="/opensearch.xml" title="GitHub">
  <link rel="fluid-icon" href="https://github.com/fluidicon.png" title="GitHub">
  <meta property="fb:app_id" content="1401488693436528">

    
    <meta content="https://avatars0.githubusercontent.com/u/16920611?v=3&amp;s=400" property="og:image" /><meta content="GitHub" property="og:site_name" /><meta content="object" property="og:type" /><meta content="hellobajie/AES-of-JavaScript" property="og:title" /><meta content="https://github.com/hellobajie/AES-of-JavaScript" property="og:url" /><meta content="AES-of-JavaScript - 两种JavaScript的AES加密方式（可与Java相互加解密）" property="og:description" />

  <link rel="assets" href="https://assets-cdn.github.com/">
  
  <meta name="pjax-timeout" content="1000">
  
  <meta name="request-id" content="2921:2F846:218BA3B:370CE89:58F030B6" data-pjax-transient>
  

  <meta name="selected-link" value="repo_source" data-pjax-transient>

  <meta name="google-site-verification" content="KT5gs8h0wvaagLKAVWq8bbeNwnZZK1r1XQysX3xurLU">
<meta name="google-site-verification" content="ZzhVyEFwb7w3e0-uOTltm8Jsck2F5StVihD0exw2fsA">
    <meta name="google-analytics" content="UA-3769691-2">

<meta content="collector.githubapp.com" name="octolytics-host" /><meta content="github" name="octolytics-app-id" /><meta content="https://collector.githubapp.com/github-external/browser_event" name="octolytics-event-url" /><meta content="2921:2F846:218BA3B:370CE89:58F030B6" name="octolytics-dimension-request_id" />
<meta content="/&lt;user-name&gt;/&lt;repo-name&gt;/blob/show" data-pjax-transient="true" name="analytics-location" />




  <meta class="js-ga-set" name="dimension1" content="Logged Out">


  

      <meta name="hostname" content="github.com">
  <meta name="user-login" content="">

      <meta name="expected-hostname" content="github.com">
    <meta name="js-proxy-site-detection-payload" content="ODI1MzA2ZWQwZWRkZDAyZjQ2ODdiOGNiNDg4OWZmYTBiZTUzZjliYTY0MDFkNjEzYzgwZDlhYzliNzNiN2NjZXx7InJlbW90ZV9hZGRyZXNzIjoiMjIyLjQyLjExNC4xNDUiLCJyZXF1ZXN0X2lkIjoiMjkyMToyRjg0NjoyMThCQTNCOjM3MENFODk6NThGMDMwQjYiLCJ0aW1lc3RhbXAiOjE0OTIxMzYxMTksImhvc3QiOiJnaXRodWIuY29tIn0=">


  <meta name="html-safe-nonce" content="9cc78ee8f21eb1f9a9c83bd86ee4faae29c796ea">

  <meta http-equiv="x-pjax-version" content="543291d3cbffec838ad53be985002a00">
  

    
  <meta name="description" content="AES-of-JavaScript - 两种JavaScript的AES加密方式（可与Java相互加解密）">
  <meta name="go-import" content="github.com/hellobajie/AES-of-JavaScript git https://github.com/hellobajie/AES-of-JavaScript.git">

  <meta content="16920611" name="octolytics-dimension-user_id" /><meta content="hellobajie" name="octolytics-dimension-user_login" /><meta content="64601329" name="octolytics-dimension-repository_id" /><meta content="hellobajie/AES-of-JavaScript" name="octolytics-dimension-repository_nwo" /><meta content="true" name="octolytics-dimension-repository_public" /><meta content="false" name="octolytics-dimension-repository_is_fork" /><meta content="64601329" name="octolytics-dimension-repository_network_root_id" /><meta content="hellobajie/AES-of-JavaScript" name="octolytics-dimension-repository_network_root_nwo" />
        <link href="https://github.com/hellobajie/AES-of-JavaScript/commits/master.atom" rel="alternate" title="Recent Commits to AES-of-JavaScript:master" type="application/atom+xml">


    <link rel="canonical" href="https://github.com/hellobajie/AES-of-JavaScript/blob/master/aes_2.js" data-pjax-transient>


  <meta name="browser-stats-url" content="https://api.github.com/_private/browser/stats">

  <meta name="browser-errors-url" content="https://api.github.com/_private/browser/errors">

  <link rel="mask-icon" href="https://assets-cdn.github.com/pinned-octocat.svg" color="#000000">
  <link rel="icon" type="image/x-icon" href="https://assets-cdn.github.com/favicon.ico">

<meta name="theme-color" content="#1e2327">


  <meta name="u2f-support" content="true">

  </head>

  <body class="logged-out env-production page-blob">
    


  <div class="position-relative js-header-wrapper ">
    <a href="#start-of-content" tabindex="1" class="accessibility-aid js-skip-to-content">Skip to content</a>
    <div id="js-pjax-loader-bar" class="pjax-loader-bar"><div class="progress"></div></div>

    
    
    



          <header class="site-header js-details-container Details" role="banner">
  <div class="container-responsive">
    <a class="header-logo-invertocat" href="https://github.com/" aria-label="Homepage" data-ga-click="(Logged out) Header, go to homepage, icon:logo-wordmark">
      <svg aria-hidden="true" class="octicon octicon-mark-github" height="32" version="1.1" viewBox="0 0 16 16" width="32"><path fill-rule="evenodd" d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.013 8.013 0 0 0 16 8c0-4.42-3.58-8-8-8z"/></svg>
    </a>

    <button class="btn-link float-right site-header-toggle js-details-target" type="button" aria-label="Toggle navigation">
      <svg aria-hidden="true" class="octicon octicon-three-bars" height="24" version="1.1" viewBox="0 0 12 16" width="18"><path fill-rule="evenodd" d="M11.41 9H.59C0 9 0 8.59 0 8c0-.59 0-1 .59-1H11.4c.59 0 .59.41.59 1 0 .59 0 1-.59 1h.01zm0-4H.59C0 5 0 4.59 0 4c0-.59 0-1 .59-1H11.4c.59 0 .59.41.59 1 0 .59 0 1-.59 1h.01zM.59 11H11.4c.59 0 .59.41.59 1 0 .59 0 1-.59 1H.59C0 13 0 12.59 0 12c0-.59 0-1 .59-1z"/></svg>
    </button>

    <div class="site-header-menu">
      <nav class="site-header-nav">
        <a href="/features" class="js-selected-navigation-item nav-item" data-ga-click="Header, click, Nav menu - item:features" data-selected-links="/features /features">
          Features
</a>        <a href="/business" class="js-selected-navigation-item nav-item" data-ga-click="Header, click, Nav menu - item:business" data-selected-links="/business /business/security /business/customers /business">
          Business
</a>        <a href="/explore" class="js-selected-navigation-item nav-item" data-ga-click="Header, click, Nav menu - item:explore" data-selected-links="/explore /trending /trending/developers /integrations /integrations/feature/code /integrations/feature/collaborate /integrations/feature/ship /showcases /explore">
          Explore
</a>        <a href="/pricing" class="js-selected-navigation-item nav-item" data-ga-click="Header, click, Nav menu - item:pricing" data-selected-links="/pricing /pricing">
          Pricing
</a>      </nav>

      <div class="site-header-actions">
          <div class="header-search scoped-search site-scoped-search js-site-search" role="search">
  <!-- '"` --><!-- </textarea></xmp> --></option></form><form accept-charset="UTF-8" action="/hellobajie/AES-of-JavaScript/search" class="js-site-search-form" data-scoped-search-url="/hellobajie/AES-of-JavaScript/search" data-unscoped-search-url="/search" method="get"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /></div>
    <label class="form-control header-search-wrapper js-chromeless-input-container">
        <a href="/hellobajie/AES-of-JavaScript/blob/master/aes_2.js" class="header-search-scope no-underline">This repository</a>
      <input type="text"
        class="form-control header-search-input js-site-search-focus js-site-search-field is-clearable"
        data-hotkey="s"
        name="q"
        value=""
        placeholder="Search"
        aria-label="Search this repository"
        data-unscoped-placeholder="Search GitHub"
        data-scoped-placeholder="Search"
        autocapitalize="off">
        <input type="hidden" class="js-site-search-type-field" name="type" >
    </label>
</form></div>


          <a class="text-bold site-header-link" href="/login?return_to=%2Fhellobajie%2FAES-of-JavaScript%2Fblob%2Fmaster%2Faes_2.js" data-ga-click="(Logged out) Header, clicked Sign in, text:sign-in">Sign in</a>
            <span class="text-gray">or</span>
            <a class="text-bold site-header-link" href="/join?source=header-repo" data-ga-click="(Logged out) Header, clicked Sign up, text:sign-up">Sign up</a>
      </div>
    </div>
  </div>
</header>


  </div>

  <div id="start-of-content" class="accessibility-aid"></div>

    <div id="js-flash-container">
</div>



  <div role="main">
        <div itemscope itemtype="http://schema.org/SoftwareSourceCode">
    <div id="js-repo-pjax-container" data-pjax-container>
        


  <div class="pagehead repohead instapaper_ignore readability-menu experiment-repo-nav">
    <div class="container repohead-details-container">


      <ul class="pagehead-actions">
  <li>
      <a href="/login?return_to=%2Fhellobajie%2FAES-of-JavaScript"
    class="btn btn-sm btn-with-count tooltipped tooltipped-n"
    aria-label="You must be signed in to watch a repository" rel="nofollow">
    <svg aria-hidden="true" class="octicon octicon-eye" height="16" version="1.1" viewBox="0 0 16 16" width="16"><path fill-rule="evenodd" d="M8.06 2C3 2 0 8 0 8s3 6 8.06 6C13 14 16 8 16 8s-3-6-7.94-6zM8 12c-2.2 0-4-1.78-4-4 0-2.2 1.8-4 4-4 2.22 0 4 1.8 4 4 0 2.22-1.78 4-4 4zm2-4c0 1.11-.89 2-2 2-1.11 0-2-.89-2-2 0-1.11.89-2 2-2 1.11 0 2 .89 2 2z"/></svg>
    Watch
  </a>
  <a class="social-count" href="/hellobajie/AES-of-JavaScript/watchers"
     aria-label="1 user is watching this repository">
    1
  </a>

  </li>

  <li>
      <a href="/login?return_to=%2Fhellobajie%2FAES-of-JavaScript"
    class="btn btn-sm btn-with-count tooltipped tooltipped-n"
    aria-label="You must be signed in to star a repository" rel="nofollow">
    <svg aria-hidden="true" class="octicon octicon-star" height="16" version="1.1" viewBox="0 0 14 16" width="14"><path fill-rule="evenodd" d="M14 6l-4.9-.64L7 1 4.9 5.36 0 6l3.6 3.26L2.67 14 7 11.67 11.33 14l-.93-4.74z"/></svg>
    Star
  </a>

    <a class="social-count js-social-count" href="/hellobajie/AES-of-JavaScript/stargazers"
      aria-label="11 users starred this repository">
      11
    </a>

  </li>

  <li>
      <a href="/login?return_to=%2Fhellobajie%2FAES-of-JavaScript"
        class="btn btn-sm btn-with-count tooltipped tooltipped-n"
        aria-label="You must be signed in to fork a repository" rel="nofollow">
        <svg aria-hidden="true" class="octicon octicon-repo-forked" height="16" version="1.1" viewBox="0 0 10 16" width="10"><path fill-rule="evenodd" d="M8 1a1.993 1.993 0 0 0-1 3.72V6L5 8 3 6V4.72A1.993 1.993 0 0 0 2 1a1.993 1.993 0 0 0-1 3.72V6.5l3 3v1.78A1.993 1.993 0 0 0 5 15a1.993 1.993 0 0 0 1-3.72V9.5l3-3V4.72A1.993 1.993 0 0 0 8 1zM2 4.2C1.34 4.2.8 3.65.8 3c0-.65.55-1.2 1.2-1.2.65 0 1.2.55 1.2 1.2 0 .65-.55 1.2-1.2 1.2zm3 10c-.66 0-1.2-.55-1.2-1.2 0-.65.55-1.2 1.2-1.2.65 0 1.2.55 1.2 1.2 0 .65-.55 1.2-1.2 1.2zm3-10c-.66 0-1.2-.55-1.2-1.2 0-.65.55-1.2 1.2-1.2.65 0 1.2.55 1.2 1.2 0 .65-.55 1.2-1.2 1.2z"/></svg>
        Fork
      </a>

    <a href="/hellobajie/AES-of-JavaScript/network" class="social-count"
       aria-label="20 users forked this repository">
      20
    </a>
  </li>
</ul>

      <h1 class="public ">
  <svg aria-hidden="true" class="octicon octicon-repo" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M4 9H3V8h1v1zm0-3H3v1h1V6zm0-2H3v1h1V4zm0-2H3v1h1V2zm8-1v12c0 .55-.45 1-1 1H6v2l-1.5-1.5L3 16v-2H1c-.55 0-1-.45-1-1V1c0-.55.45-1 1-1h10c.55 0 1 .45 1 1zm-1 10H1v2h2v-1h3v1h5v-2zm0-10H2v9h9V1z"/></svg>
  <span class="author" itemprop="author"><a href="/hellobajie" class="url fn" rel="author">hellobajie</a></span><!--
--><span class="path-divider">/</span><!--
--><strong itemprop="name"><a href="/hellobajie/AES-of-JavaScript" data-pjax="#js-repo-pjax-container">AES-of-JavaScript</a></strong>

</h1>

    </div>
    <div class="container">
      
<nav class="reponav js-repo-nav js-sidenav-container-pjax"
     itemscope
     itemtype="http://schema.org/BreadcrumbList"
     role="navigation"
     data-pjax="#js-repo-pjax-container">

  <span itemscope itemtype="http://schema.org/ListItem" itemprop="itemListElement">
    <a href="/hellobajie/AES-of-JavaScript" class="js-selected-navigation-item selected reponav-item" data-hotkey="g c" data-selected-links="repo_source repo_downloads repo_commits repo_releases repo_tags repo_branches /hellobajie/AES-of-JavaScript" itemprop="url">
      <svg aria-hidden="true" class="octicon octicon-code" height="16" version="1.1" viewBox="0 0 14 16" width="14"><path fill-rule="evenodd" d="M9.5 3L8 4.5 11.5 8 8 11.5 9.5 13 14 8 9.5 3zm-5 0L0 8l4.5 5L6 11.5 2.5 8 6 4.5 4.5 3z"/></svg>
      <span itemprop="name">Code</span>
      <meta itemprop="position" content="1">
</a>  </span>

    <span itemscope itemtype="http://schema.org/ListItem" itemprop="itemListElement">
      <a href="/hellobajie/AES-of-JavaScript/issues" class="js-selected-navigation-item reponav-item" data-hotkey="g i" data-selected-links="repo_issues repo_labels repo_milestones /hellobajie/AES-of-JavaScript/issues" itemprop="url">
        <svg aria-hidden="true" class="octicon octicon-issue-opened" height="16" version="1.1" viewBox="0 0 14 16" width="14"><path fill-rule="evenodd" d="M7 2.3c3.14 0 5.7 2.56 5.7 5.7s-2.56 5.7-5.7 5.7A5.71 5.71 0 0 1 1.3 8c0-3.14 2.56-5.7 5.7-5.7zM7 1C3.14 1 0 4.14 0 8s3.14 7 7 7 7-3.14 7-7-3.14-7-7-7zm1 3H6v5h2V4zm0 6H6v2h2v-2z"/></svg>
        <span itemprop="name">Issues</span>
        <span class="counter">0</span>
        <meta itemprop="position" content="2">
</a>    </span>

  <span itemscope itemtype="http://schema.org/ListItem" itemprop="itemListElement">
    <a href="/hellobajie/AES-of-JavaScript/pulls" class="js-selected-navigation-item reponav-item" data-hotkey="g p" data-selected-links="repo_pulls /hellobajie/AES-of-JavaScript/pulls" itemprop="url">
      <svg aria-hidden="true" class="octicon octicon-git-pull-request" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M11 11.28V5c-.03-.78-.34-1.47-.94-2.06C9.46 2.35 8.78 2.03 8 2H7V0L4 3l3 3V4h1c.27.02.48.11.69.31.21.2.3.42.31.69v6.28A1.993 1.993 0 0 0 10 15a1.993 1.993 0 0 0 1-3.72zm-1 2.92c-.66 0-1.2-.55-1.2-1.2 0-.65.55-1.2 1.2-1.2.65 0 1.2.55 1.2 1.2 0 .65-.55 1.2-1.2 1.2zM4 3c0-1.11-.89-2-2-2a1.993 1.993 0 0 0-1 3.72v6.56A1.993 1.993 0 0 0 2 15a1.993 1.993 0 0 0 1-3.72V4.72c.59-.34 1-.98 1-1.72zm-.8 10c0 .66-.55 1.2-1.2 1.2-.65 0-1.2-.55-1.2-1.2 0-.65.55-1.2 1.2-1.2.65 0 1.2.55 1.2 1.2zM2 4.2C1.34 4.2.8 3.65.8 3c0-.65.55-1.2 1.2-1.2.65 0 1.2.55 1.2 1.2 0 .65-.55 1.2-1.2 1.2z"/></svg>
      <span itemprop="name">Pull requests</span>
      <span class="counter">0</span>
      <meta itemprop="position" content="3">
</a>  </span>

    <a href="/hellobajie/AES-of-JavaScript/projects" class="js-selected-navigation-item reponav-item" data-selected-links="repo_projects new_repo_project repo_project /hellobajie/AES-of-JavaScript/projects">
      <svg aria-hidden="true" class="octicon octicon-project" height="16" version="1.1" viewBox="0 0 15 16" width="15"><path fill-rule="evenodd" d="M10 12h3V2h-3v10zm-4-2h3V2H6v8zm-4 4h3V2H2v12zm-1 1h13V1H1v14zM14 0H1a1 1 0 0 0-1 1v14a1 1 0 0 0 1 1h13a1 1 0 0 0 1-1V1a1 1 0 0 0-1-1z"/></svg>
      Projects
      <span class="counter">0</span>
</a>


  <a href="/hellobajie/AES-of-JavaScript/pulse" class="js-selected-navigation-item reponav-item" data-selected-links="pulse /hellobajie/AES-of-JavaScript/pulse">
    <svg aria-hidden="true" class="octicon octicon-pulse" height="16" version="1.1" viewBox="0 0 14 16" width="14"><path fill-rule="evenodd" d="M11.5 8L8.8 5.4 6.6 8.5 5.5 1.6 2.38 8H0v2h3.6l.9-1.8.9 5.4L9 8.5l1.6 1.5H14V8z"/></svg>
    Pulse
</a>
  <a href="/hellobajie/AES-of-JavaScript/graphs" class="js-selected-navigation-item reponav-item" data-selected-links="repo_graphs repo_contributors /hellobajie/AES-of-JavaScript/graphs">
    <svg aria-hidden="true" class="octicon octicon-graph" height="16" version="1.1" viewBox="0 0 16 16" width="16"><path fill-rule="evenodd" d="M16 14v1H0V0h1v14h15zM5 13H3V8h2v5zm4 0H7V3h2v10zm4 0h-2V6h2v7z"/></svg>
    Graphs
</a>

</nav>

    </div>
  </div>

<div class="container new-discussion-timeline experiment-repo-nav">
  <div class="repository-content">

    
          

<a href="/hellobajie/AES-of-JavaScript/blob/9838d891e3474c1a9e09e5c5edba86c4d1ce11d3/aes_2.js" class="d-none js-permalink-shortcut" data-hotkey="y">Permalink</a>

<!-- blob contrib key: blob_contributors:v21:7789bed9b2f39b1914550d0fded14bd9 -->

<div class="file-navigation js-zeroclipboard-container">
  
<div class="select-menu branch-select-menu js-menu-container js-select-menu float-left">
  <button class=" btn btn-sm select-menu-button js-menu-target css-truncate" data-hotkey="w"
    
    type="button" aria-label="Switch branches or tags" tabindex="0" aria-haspopup="true">
      <i>Branch:</i>
      <span class="js-select-button css-truncate-target">master</span>
  </button>

  <div class="select-menu-modal-holder js-menu-content js-navigation-container" data-pjax>

    <div class="select-menu-modal">
      <div class="select-menu-header">
        <svg aria-label="Close" class="octicon octicon-x js-menu-close" height="16" role="img" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M7.48 8l3.75 3.75-1.48 1.48L6 9.48l-3.75 3.75-1.48-1.48L4.52 8 .77 4.25l1.48-1.48L6 6.52l3.75-3.75 1.48 1.48z"/></svg>
        <span class="select-menu-title">Switch branches/tags</span>
      </div>

      <div class="select-menu-filters">
        <div class="select-menu-text-filter">
          <input type="text" aria-label="Filter branches/tags" id="context-commitish-filter-field" class="form-control js-filterable-field js-navigation-enable" placeholder="Filter branches/tags">
        </div>
        <div class="select-menu-tabs">
          <ul>
            <li class="select-menu-tab">
              <a href="#" data-tab-filter="branches" data-filter-placeholder="Filter branches/tags" class="js-select-menu-tab" role="tab">Branches</a>
            </li>
            <li class="select-menu-tab">
              <a href="#" data-tab-filter="tags" data-filter-placeholder="Find a tag…" class="js-select-menu-tab" role="tab">Tags</a>
            </li>
          </ul>
        </div>
      </div>

      <div class="select-menu-list select-menu-tab-bucket js-select-menu-tab-bucket" data-tab-filter="branches" role="menu">

        <div data-filterable-for="context-commitish-filter-field" data-filterable-type="substring">


            <a class="select-menu-item js-navigation-item js-navigation-open selected"
               href="/hellobajie/AES-of-JavaScript/blob/master/aes_2.js"
               data-name="master"
               data-skip-pjax="true"
               rel="nofollow">
              <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 5l-8 8-4-4 1.5-1.5L4 10l6.5-6.5z"/></svg>
              <span class="select-menu-item-text css-truncate-target js-select-menu-filter-text">
                master
              </span>
            </a>
        </div>

          <div class="select-menu-no-results">Nothing to show</div>
      </div>

      <div class="select-menu-list select-menu-tab-bucket js-select-menu-tab-bucket" data-tab-filter="tags">
        <div data-filterable-for="context-commitish-filter-field" data-filterable-type="substring">


        </div>

        <div class="select-menu-no-results">Nothing to show</div>
      </div>

    </div>
  </div>
</div>

  <div class="BtnGroup float-right">
    <a href="/hellobajie/AES-of-JavaScript/find/master"
          class="js-pjax-capture-input btn btn-sm BtnGroup-item"
          data-pjax
          data-hotkey="t">
      Find file
    </a>
    <button aria-label="Copy file path to clipboard" class="js-zeroclipboard btn btn-sm BtnGroup-item tooltipped tooltipped-s" data-copied-hint="Copied!" type="button">Copy path</button>
  </div>
  <div class="breadcrumb js-zeroclipboard-target">
    <span class="repo-root js-repo-root"><span class="js-path-segment"><a href="/hellobajie/AES-of-JavaScript"><span>AES-of-JavaScript</span></a></span></span><span class="separator">/</span><strong class="final-path">aes_2.js</strong>
  </div>
</div>



  <div class="commit-tease">
      <span class="float-right">
        <a class="commit-tease-sha" href="/hellobajie/AES-of-JavaScript/commit/9838d891e3474c1a9e09e5c5edba86c4d1ce11d3" data-pjax>
          9838d89
        </a>
        <relative-time datetime="2016-08-01T09:09:45Z">Aug 1, 2016</relative-time>
      </span>
      <div>
        <img alt="@hellobajie" class="avatar" height="20" src="https://avatars1.githubusercontent.com/u/16920611?v=3&amp;s=40" width="20" />
        <a href="/hellobajie" class="user-mention" rel="author">hellobajie</a>
          <a href="/hellobajie/AES-of-JavaScript/commit/9838d891e3474c1a9e09e5c5edba86c4d1ce11d3" class="message" data-pjax="true" title="aes_2">aes_2</a>
      </div>

    <div class="commit-tease-contributors">
      <button type="button" class="btn-link muted-link contributors-toggle" data-facebox="#blob_contributors_box">
        <strong>1</strong>
         contributor
      </button>
      
    </div>

    <div id="blob_contributors_box" style="display:none">
      <h2 class="facebox-header" data-facebox-id="facebox-header">Users who have contributed to this file</h2>
      <ul class="facebox-user-list" data-facebox-id="facebox-description">
          <li class="facebox-user-list-item">
            <img alt="@hellobajie" height="24" src="https://avatars3.githubusercontent.com/u/16920611?v=3&amp;s=48" width="24" />
            <a href="/hellobajie">hellobajie</a>
          </li>
      </ul>
    </div>
  </div>

<div class="file">
  <div class="file-header">
  <div class="file-actions">

    <div class="BtnGroup">
      <a href="/hellobajie/AES-of-JavaScript/raw/master/aes_2.js" class="btn btn-sm BtnGroup-item" id="raw-url">Raw</a>
        <a href="/hellobajie/AES-of-JavaScript/blame/master/aes_2.js" class="btn btn-sm js-update-url-with-hash BtnGroup-item" data-hotkey="b">Blame</a>
      <a href="/hellobajie/AES-of-JavaScript/commits/master/aes_2.js" class="btn btn-sm BtnGroup-item" rel="nofollow">History</a>
    </div>

        <a class="btn-octicon tooltipped tooltipped-nw"
           href="https://windows.github.com"
           aria-label="Open this file in GitHub Desktop"
           data-ga-click="Repository, open with desktop, type:windows">
            <svg aria-hidden="true" class="octicon octicon-device-desktop" height="16" version="1.1" viewBox="0 0 16 16" width="16"><path fill-rule="evenodd" d="M15 2H1c-.55 0-1 .45-1 1v9c0 .55.45 1 1 1h5.34c-.25.61-.86 1.39-2.34 2h8c-1.48-.61-2.09-1.39-2.34-2H15c.55 0 1-.45 1-1V3c0-.55-.45-1-1-1zm0 9H1V3h14v8z"/></svg>
        </a>

        <button type="button" class="btn-octicon disabled tooltipped tooltipped-nw"
          aria-label="You must be signed in to make or propose changes">
          <svg aria-hidden="true" class="octicon octicon-pencil" height="16" version="1.1" viewBox="0 0 14 16" width="14"><path fill-rule="evenodd" d="M0 12v3h3l8-8-3-3-8 8zm3 2H1v-2h1v1h1v1zm10.3-9.3L12 6 9 3l1.3-1.3a.996.996 0 0 1 1.41 0l1.59 1.59c.39.39.39 1.02 0 1.41z"/></svg>
        </button>
        <button type="button" class="btn-octicon btn-octicon-danger disabled tooltipped tooltipped-nw"
          aria-label="You must be signed in to make or propose changes">
          <svg aria-hidden="true" class="octicon octicon-trashcan" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M11 2H9c0-.55-.45-1-1-1H5c-.55 0-1 .45-1 1H2c-.55 0-1 .45-1 1v1c0 .55.45 1 1 1v9c0 .55.45 1 1 1h7c.55 0 1-.45 1-1V5c.55 0 1-.45 1-1V3c0-.55-.45-1-1-1zm-1 12H3V5h1v8h1V5h1v8h1V5h1v8h1V5h1v9zm1-10H2V3h9v1z"/></svg>
        </button>
  </div>

  <div class="file-info">
      36 lines (35 sloc)
      <span class="file-info-divider"></span>
    13 KB
  </div>
</div>

  

  <div itemprop="text" class="blob-wrapper data type-javascript">
      <table class="highlight tab-size js-file-line-container" data-tab-size="8">
      <tr>
        <td id="L1" class="blob-num js-line-number" data-line-number="1"></td>
        <td id="LC1" class="blob-code blob-code-inner js-file-line"><span class="pl-c">/*</span></td>
      </tr>
      <tr>
        <td id="L2" class="blob-num js-line-number" data-line-number="2"></td>
        <td id="LC2" class="blob-code blob-code-inner js-file-line"><span class="pl-c">CryptoJS v3.1.2</span></td>
      </tr>
      <tr>
        <td id="L3" class="blob-num js-line-number" data-line-number="3"></td>
        <td id="LC3" class="blob-code blob-code-inner js-file-line"><span class="pl-c">code.google.com/p/crypto-js</span></td>
      </tr>
      <tr>
        <td id="L4" class="blob-num js-line-number" data-line-number="4"></td>
        <td id="LC4" class="blob-code blob-code-inner js-file-line"><span class="pl-c">(c) 2009-2013 by Jeff Mott. All rights reserved.</span></td>
      </tr>
      <tr>
        <td id="L5" class="blob-num js-line-number" data-line-number="5"></td>
        <td id="LC5" class="blob-code blob-code-inner js-file-line"><span class="pl-c">code.google.com/p/crypto-js/wiki/License</span></td>
      </tr>
      <tr>
        <td id="L6" class="blob-num js-line-number" data-line-number="6"></td>
        <td id="LC6" class="blob-code blob-code-inner js-file-line"><span class="pl-c">*/</span></td>
      </tr>
      <tr>
        <td id="L7" class="blob-num js-line-number" data-line-number="7"></td>
        <td id="LC7" class="blob-code blob-code-inner js-file-line"><span class="pl-k">var</span> CryptoJS<span class="pl-k">=</span>CryptoJS<span class="pl-k">||</span><span class="pl-k">function</span>(<span class="pl-smi">u</span>,<span class="pl-smi">p</span>){<span class="pl-k">var</span> d<span class="pl-k">=</span>{},l<span class="pl-k">=</span><span class="pl-smi">d</span>.<span class="pl-smi">lib</span><span class="pl-k">=</span>{},<span class="pl-en">s</span><span class="pl-k">=</span><span class="pl-k">function</span>(){},t<span class="pl-k">=</span><span class="pl-smi">l</span>.<span class="pl-smi">Base</span><span class="pl-k">=</span>{<span class="pl-en">extend</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">a</span>){<span class="pl-smi">s</span>.<span class="pl-c1">prototype</span><span class="pl-k">=</span><span class="pl-c1">this</span>;<span class="pl-k">var</span> c<span class="pl-k">=</span><span class="pl-k">new</span> <span class="pl-en">s</span>;a<span class="pl-k">&amp;&amp;</span><span class="pl-smi">c</span>.<span class="pl-en">mixIn</span>(a);<span class="pl-smi">c</span>.<span class="pl-en">hasOwnProperty</span>(<span class="pl-s"><span class="pl-pds">&quot;</span>init<span class="pl-pds">&quot;</span></span>)<span class="pl-k">||</span>(<span class="pl-smi">c</span>.<span class="pl-en">init</span><span class="pl-k">=</span><span class="pl-k">function</span>(){<span class="pl-smi">c</span>.<span class="pl-smi">$super</span>.<span class="pl-smi">init</span>.<span class="pl-c1">apply</span>(<span class="pl-c1">this</span>,<span class="pl-c1">arguments</span>)});<span class="pl-smi">c</span>.<span class="pl-smi">init</span>.<span class="pl-c1">prototype</span><span class="pl-k">=</span>c;<span class="pl-smi">c</span>.<span class="pl-smi">$super</span><span class="pl-k">=</span><span class="pl-c1">this</span>;<span class="pl-k">return</span> c},<span class="pl-en">create</span><span class="pl-k">:</span><span class="pl-k">function</span>(){<span class="pl-k">var</span> a<span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-en">extend</span>();<span class="pl-smi">a</span>.<span class="pl-smi">init</span>.<span class="pl-c1">apply</span>(a,<span class="pl-c1">arguments</span>);<span class="pl-k">return</span> a},<span class="pl-en">init</span><span class="pl-k">:</span><span class="pl-k">function</span>(){},<span class="pl-en">mixIn</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">a</span>){<span class="pl-k">for</span>(<span class="pl-k">var</span> c <span class="pl-k">in</span> a)<span class="pl-smi">a</span>.<span class="pl-en">hasOwnProperty</span>(c)<span class="pl-k">&amp;&amp;</span>(<span class="pl-c1">this</span>[c]<span class="pl-k">=</span>a[c]);<span class="pl-smi">a</span>.<span class="pl-en">hasOwnProperty</span>(<span class="pl-s"><span class="pl-pds">&quot;</span>toString<span class="pl-pds">&quot;</span></span>)<span class="pl-k">&amp;&amp;</span>(<span class="pl-c1">this</span>.<span class="pl-smi">toString</span><span class="pl-k">=</span><span class="pl-smi">a</span>.<span class="pl-smi">toString</span>)},<span class="pl-en">clone</span><span class="pl-k">:</span><span class="pl-k">function</span>(){<span class="pl-k">return</span> <span class="pl-c1">this</span>.<span class="pl-smi">init</span>.<span class="pl-c1">prototype</span>.<span class="pl-en">extend</span>(<span class="pl-c1">this</span>)}},</td>
      </tr>
      <tr>
        <td id="L8" class="blob-num js-line-number" data-line-number="8"></td>
        <td id="LC8" class="blob-code blob-code-inner js-file-line">r<span class="pl-k">=</span><span class="pl-smi">l</span>.<span class="pl-smi">WordArray</span><span class="pl-k">=</span><span class="pl-smi">t</span>.<span class="pl-en">extend</span>({<span class="pl-en">init</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">a</span>,<span class="pl-smi">c</span>){a<span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-smi">words</span><span class="pl-k">=</span>a<span class="pl-k">||</span>[];<span class="pl-c1">this</span>.<span class="pl-smi">sigBytes</span><span class="pl-k">=</span>c<span class="pl-k">!=</span>p<span class="pl-k">?</span>c<span class="pl-k">:</span><span class="pl-c1">4</span><span class="pl-k">*</span><span class="pl-smi">a</span>.<span class="pl-c1">length</span>},<span class="pl-en">toString</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">a</span>){<span class="pl-k">return</span>(a<span class="pl-k">||</span>v).<span class="pl-en">stringify</span>(<span class="pl-c1">this</span>)},<span class="pl-en">concat</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">a</span>){<span class="pl-k">var</span> c<span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-smi">words</span>,e<span class="pl-k">=</span><span class="pl-smi">a</span>.<span class="pl-smi">words</span>,j<span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-smi">sigBytes</span>;a<span class="pl-k">=</span><span class="pl-smi">a</span>.<span class="pl-smi">sigBytes</span>;<span class="pl-c1">this</span>.<span class="pl-en">clamp</span>();<span class="pl-k">if</span>(j<span class="pl-k">%</span><span class="pl-c1">4</span>)<span class="pl-k">for</span>(<span class="pl-k">var</span> k<span class="pl-k">=</span><span class="pl-c1">0</span>;k<span class="pl-k">&lt;</span>a;k<span class="pl-k">++</span>)c[j<span class="pl-k">+</span>k<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">2</span>]<span class="pl-k">|=</span>(e[k<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">2</span>]<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">24</span><span class="pl-k">-</span><span class="pl-c1">8</span><span class="pl-k">*</span>(k<span class="pl-k">%</span><span class="pl-c1">4</span>)<span class="pl-k">&amp;</span><span class="pl-c1">255</span>)<span class="pl-k">&lt;&lt;</span><span class="pl-c1">24</span><span class="pl-k">-</span><span class="pl-c1">8</span><span class="pl-k">*</span>((j<span class="pl-k">+</span>k)<span class="pl-k">%</span><span class="pl-c1">4</span>);<span class="pl-k">else</span> <span class="pl-k">if</span>(<span class="pl-c1">65535</span><span class="pl-k">&lt;</span><span class="pl-smi">e</span>.<span class="pl-c1">length</span>)<span class="pl-k">for</span>(k<span class="pl-k">=</span><span class="pl-c1">0</span>;k<span class="pl-k">&lt;</span>a;k<span class="pl-k">+=</span><span class="pl-c1">4</span>)c[j<span class="pl-k">+</span>k<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">2</span>]<span class="pl-k">=</span>e[k<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">2</span>];<span class="pl-k">else</span> <span class="pl-smi">c</span>.<span class="pl-smi">push</span>.<span class="pl-c1">apply</span>(c,e);<span class="pl-c1">this</span>.<span class="pl-smi">sigBytes</span><span class="pl-k">+=</span>a;<span class="pl-k">return</span> <span class="pl-c1">this</span>},<span class="pl-en">clamp</span><span class="pl-k">:</span><span class="pl-k">function</span>(){<span class="pl-k">var</span> a<span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-smi">words</span>,c<span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-smi">sigBytes</span>;a[c<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">2</span>]<span class="pl-k">&amp;=</span><span class="pl-c1">4294967295</span><span class="pl-k">&lt;&lt;</span></td>
      </tr>
      <tr>
        <td id="L9" class="blob-num js-line-number" data-line-number="9"></td>
        <td id="LC9" class="blob-code blob-code-inner js-file-line"><span class="pl-c1">32</span><span class="pl-k">-</span><span class="pl-c1">8</span><span class="pl-k">*</span>(c<span class="pl-k">%</span><span class="pl-c1">4</span>);<span class="pl-smi">a</span>.<span class="pl-c1">length</span><span class="pl-k">=</span><span class="pl-smi">u</span>.<span class="pl-en">ceil</span>(c<span class="pl-k">/</span><span class="pl-c1">4</span>)},<span class="pl-en">clone</span><span class="pl-k">:</span><span class="pl-k">function</span>(){<span class="pl-k">var</span> a<span class="pl-k">=</span><span class="pl-smi">t</span>.<span class="pl-smi">clone</span>.<span class="pl-c1">call</span>(<span class="pl-c1">this</span>);<span class="pl-smi">a</span>.<span class="pl-smi">words</span><span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-smi">words</span>.<span class="pl-c1">slice</span>(<span class="pl-c1">0</span>);<span class="pl-k">return</span> a},<span class="pl-en">random</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">a</span>){<span class="pl-k">for</span>(<span class="pl-k">var</span> c<span class="pl-k">=</span>[],e<span class="pl-k">=</span><span class="pl-c1">0</span>;e<span class="pl-k">&lt;</span>a;e<span class="pl-k">+=</span><span class="pl-c1">4</span>)<span class="pl-smi">c</span>.<span class="pl-c1">push</span>(<span class="pl-c1">4294967296</span><span class="pl-k">*</span><span class="pl-smi">u</span>.<span class="pl-en">random</span>()<span class="pl-k">|</span><span class="pl-c1">0</span>);<span class="pl-k">return</span> <span class="pl-k">new</span> <span class="pl-en">r.init</span>(c,a)}}),w<span class="pl-k">=</span><span class="pl-smi">d</span>.<span class="pl-smi">enc</span><span class="pl-k">=</span>{},v<span class="pl-k">=</span><span class="pl-smi">w</span>.<span class="pl-smi">Hex</span><span class="pl-k">=</span>{<span class="pl-en">stringify</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">a</span>){<span class="pl-k">var</span> c<span class="pl-k">=</span><span class="pl-smi">a</span>.<span class="pl-smi">words</span>;a<span class="pl-k">=</span><span class="pl-smi">a</span>.<span class="pl-smi">sigBytes</span>;<span class="pl-k">for</span>(<span class="pl-k">var</span> e<span class="pl-k">=</span>[],j<span class="pl-k">=</span><span class="pl-c1">0</span>;j<span class="pl-k">&lt;</span>a;j<span class="pl-k">++</span>){<span class="pl-k">var</span> k<span class="pl-k">=</span>c[j<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">2</span>]<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">24</span><span class="pl-k">-</span><span class="pl-c1">8</span><span class="pl-k">*</span>(j<span class="pl-k">%</span><span class="pl-c1">4</span>)<span class="pl-k">&amp;</span><span class="pl-c1">255</span>;<span class="pl-smi">e</span>.<span class="pl-c1">push</span>((k<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">4</span>).<span class="pl-c1">toString</span>(<span class="pl-c1">16</span>));<span class="pl-smi">e</span>.<span class="pl-c1">push</span>((k<span class="pl-k">&amp;</span><span class="pl-c1">15</span>).<span class="pl-c1">toString</span>(<span class="pl-c1">16</span>))}<span class="pl-k">return</span> <span class="pl-smi">e</span>.<span class="pl-c1">join</span>(<span class="pl-s"><span class="pl-pds">&quot;</span><span class="pl-pds">&quot;</span></span>)},<span class="pl-en">parse</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">a</span>){<span class="pl-k">for</span>(<span class="pl-k">var</span> c<span class="pl-k">=</span><span class="pl-smi">a</span>.<span class="pl-c1">length</span>,e<span class="pl-k">=</span>[],j<span class="pl-k">=</span><span class="pl-c1">0</span>;j<span class="pl-k">&lt;</span>c;j<span class="pl-k">+=</span><span class="pl-c1">2</span>)e[j<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">3</span>]<span class="pl-k">|=</span><span class="pl-c1">parseInt</span>(<span class="pl-smi">a</span>.<span class="pl-c1">substr</span>(j,</td>
      </tr>
      <tr>
        <td id="L10" class="blob-num js-line-number" data-line-number="10"></td>
        <td id="LC10" class="blob-code blob-code-inner js-file-line"><span class="pl-c1">2</span>),<span class="pl-c1">16</span>)<span class="pl-k">&lt;&lt;</span><span class="pl-c1">24</span><span class="pl-k">-</span><span class="pl-c1">4</span><span class="pl-k">*</span>(j<span class="pl-k">%</span><span class="pl-c1">8</span>);<span class="pl-k">return</span> <span class="pl-k">new</span> <span class="pl-en">r.init</span>(e,c<span class="pl-k">/</span><span class="pl-c1">2</span>)}},b<span class="pl-k">=</span><span class="pl-smi">w</span>.<span class="pl-smi">Latin1</span><span class="pl-k">=</span>{<span class="pl-en">stringify</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">a</span>){<span class="pl-k">var</span> c<span class="pl-k">=</span><span class="pl-smi">a</span>.<span class="pl-smi">words</span>;a<span class="pl-k">=</span><span class="pl-smi">a</span>.<span class="pl-smi">sigBytes</span>;<span class="pl-k">for</span>(<span class="pl-k">var</span> e<span class="pl-k">=</span>[],j<span class="pl-k">=</span><span class="pl-c1">0</span>;j<span class="pl-k">&lt;</span>a;j<span class="pl-k">++</span>)<span class="pl-smi">e</span>.<span class="pl-c1">push</span>(<span class="pl-c1">String</span>.<span class="pl-c1">fromCharCode</span>(c[j<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">2</span>]<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">24</span><span class="pl-k">-</span><span class="pl-c1">8</span><span class="pl-k">*</span>(j<span class="pl-k">%</span><span class="pl-c1">4</span>)<span class="pl-k">&amp;</span><span class="pl-c1">255</span>));<span class="pl-k">return</span> <span class="pl-smi">e</span>.<span class="pl-c1">join</span>(<span class="pl-s"><span class="pl-pds">&quot;</span><span class="pl-pds">&quot;</span></span>)},<span class="pl-en">parse</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">a</span>){<span class="pl-k">for</span>(<span class="pl-k">var</span> c<span class="pl-k">=</span><span class="pl-smi">a</span>.<span class="pl-c1">length</span>,e<span class="pl-k">=</span>[],j<span class="pl-k">=</span><span class="pl-c1">0</span>;j<span class="pl-k">&lt;</span>c;j<span class="pl-k">++</span>)e[j<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">2</span>]<span class="pl-k">|=</span>(<span class="pl-smi">a</span>.<span class="pl-c1">charCodeAt</span>(j)<span class="pl-k">&amp;</span><span class="pl-c1">255</span>)<span class="pl-k">&lt;&lt;</span><span class="pl-c1">24</span><span class="pl-k">-</span><span class="pl-c1">8</span><span class="pl-k">*</span>(j<span class="pl-k">%</span><span class="pl-c1">4</span>);<span class="pl-k">return</span> <span class="pl-k">new</span> <span class="pl-en">r.init</span>(e,c)}},x<span class="pl-k">=</span><span class="pl-smi">w</span>.<span class="pl-smi">Utf8</span><span class="pl-k">=</span>{<span class="pl-en">stringify</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">a</span>){<span class="pl-k">try</span>{<span class="pl-k">return</span> <span class="pl-c1">decodeURIComponent</span>(<span class="pl-c1">escape</span>(<span class="pl-smi">b</span>.<span class="pl-en">stringify</span>(a)))}<span class="pl-k">catch</span>(c){<span class="pl-k">throw</span> <span class="pl-c1">Error</span>(<span class="pl-s"><span class="pl-pds">&quot;</span>Malformed UTF-8 data<span class="pl-pds">&quot;</span></span>);}},<span class="pl-en">parse</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">a</span>){<span class="pl-k">return</span> <span class="pl-smi">b</span>.<span class="pl-c1">parse</span>(<span class="pl-c1">unescape</span>(<span class="pl-c1">encodeURIComponent</span>(a)))}},</td>
      </tr>
      <tr>
        <td id="L11" class="blob-num js-line-number" data-line-number="11"></td>
        <td id="LC11" class="blob-code blob-code-inner js-file-line">q<span class="pl-k">=</span><span class="pl-smi">l</span>.<span class="pl-smi">BufferedBlockAlgorithm</span><span class="pl-k">=</span><span class="pl-smi">t</span>.<span class="pl-en">extend</span>({<span class="pl-en">reset</span><span class="pl-k">:</span><span class="pl-k">function</span>(){<span class="pl-c1">this</span>.<span class="pl-smi">_data</span><span class="pl-k">=</span><span class="pl-k">new</span> <span class="pl-en">r.init</span>;<span class="pl-c1">this</span>.<span class="pl-smi">_nDataBytes</span><span class="pl-k">=</span><span class="pl-c1">0</span>},<span class="pl-en">_append</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">a</span>){<span class="pl-s"><span class="pl-pds">&quot;</span>string<span class="pl-pds">&quot;</span></span><span class="pl-k">==</span><span class="pl-k">typeof</span> a<span class="pl-k">&amp;&amp;</span>(a<span class="pl-k">=</span><span class="pl-smi">x</span>.<span class="pl-c1">parse</span>(a));<span class="pl-c1">this</span>.<span class="pl-smi">_data</span>.<span class="pl-c1">concat</span>(a);<span class="pl-c1">this</span>.<span class="pl-smi">_nDataBytes</span><span class="pl-k">+=</span><span class="pl-smi">a</span>.<span class="pl-smi">sigBytes</span>},<span class="pl-en">_process</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">a</span>){<span class="pl-k">var</span> c<span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-smi">_data</span>,e<span class="pl-k">=</span><span class="pl-smi">c</span>.<span class="pl-smi">words</span>,j<span class="pl-k">=</span><span class="pl-smi">c</span>.<span class="pl-smi">sigBytes</span>,k<span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-smi">blockSize</span>,b<span class="pl-k">=</span>j<span class="pl-k">/</span>(<span class="pl-c1">4</span><span class="pl-k">*</span>k),b<span class="pl-k">=</span>a<span class="pl-k">?</span><span class="pl-smi">u</span>.<span class="pl-en">ceil</span>(b)<span class="pl-k">:</span><span class="pl-smi">u</span>.<span class="pl-en">max</span>((b<span class="pl-k">|</span><span class="pl-c1">0</span>)<span class="pl-k">-</span><span class="pl-c1">this</span>.<span class="pl-smi">_minBufferSize</span>,<span class="pl-c1">0</span>);a<span class="pl-k">=</span>b<span class="pl-k">*</span>k;j<span class="pl-k">=</span><span class="pl-smi">u</span>.<span class="pl-en">min</span>(<span class="pl-c1">4</span><span class="pl-k">*</span>a,j);<span class="pl-k">if</span>(a){<span class="pl-k">for</span>(<span class="pl-k">var</span> q<span class="pl-k">=</span><span class="pl-c1">0</span>;q<span class="pl-k">&lt;</span>a;q<span class="pl-k">+=</span>k)<span class="pl-c1">this</span>.<span class="pl-en">_doProcessBlock</span>(e,q);q<span class="pl-k">=</span><span class="pl-smi">e</span>.<span class="pl-c1">splice</span>(<span class="pl-c1">0</span>,a);<span class="pl-smi">c</span>.<span class="pl-smi">sigBytes</span><span class="pl-k">-=</span>j}<span class="pl-k">return</span> <span class="pl-k">new</span> <span class="pl-en">r.init</span>(q,j)},<span class="pl-en">clone</span><span class="pl-k">:</span><span class="pl-k">function</span>(){<span class="pl-k">var</span> a<span class="pl-k">=</span><span class="pl-smi">t</span>.<span class="pl-smi">clone</span>.<span class="pl-c1">call</span>(<span class="pl-c1">this</span>);</td>
      </tr>
      <tr>
        <td id="L12" class="blob-num js-line-number" data-line-number="12"></td>
        <td id="LC12" class="blob-code blob-code-inner js-file-line"><span class="pl-smi">a</span>.<span class="pl-smi">_data</span><span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-smi">_data</span>.<span class="pl-en">clone</span>();<span class="pl-k">return</span> a},_minBufferSize<span class="pl-k">:</span><span class="pl-c1">0</span>});<span class="pl-smi">l</span>.<span class="pl-smi">Hasher</span><span class="pl-k">=</span><span class="pl-smi">q</span>.<span class="pl-en">extend</span>({cfg<span class="pl-k">:</span><span class="pl-smi">t</span>.<span class="pl-en">extend</span>(),<span class="pl-en">init</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">a</span>){<span class="pl-c1">this</span>.<span class="pl-smi">cfg</span><span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-smi">cfg</span>.<span class="pl-en">extend</span>(a);<span class="pl-c1">this</span>.<span class="pl-c1">reset</span>()},<span class="pl-en">reset</span><span class="pl-k">:</span><span class="pl-k">function</span>(){<span class="pl-smi">q</span>.<span class="pl-smi">reset</span>.<span class="pl-c1">call</span>(<span class="pl-c1">this</span>);<span class="pl-c1">this</span>.<span class="pl-en">_doReset</span>()},<span class="pl-en">update</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">a</span>){<span class="pl-c1">this</span>.<span class="pl-en">_append</span>(a);<span class="pl-c1">this</span>.<span class="pl-en">_process</span>();<span class="pl-k">return</span> <span class="pl-c1">this</span>},<span class="pl-en">finalize</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">a</span>){a<span class="pl-k">&amp;&amp;</span><span class="pl-c1">this</span>.<span class="pl-en">_append</span>(a);<span class="pl-k">return</span> <span class="pl-c1">this</span>.<span class="pl-en">_doFinalize</span>()},blockSize<span class="pl-k">:</span><span class="pl-c1">16</span>,<span class="pl-en">_createHelper</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">a</span>){<span class="pl-k">return</span> <span class="pl-k">function</span>(<span class="pl-smi">b</span>,<span class="pl-smi">e</span>){<span class="pl-k">return</span>(<span class="pl-k">new</span> <span class="pl-en">a.init</span>(e)).<span class="pl-en">finalize</span>(b)}},<span class="pl-en">_createHmacHelper</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">a</span>){<span class="pl-k">return</span> <span class="pl-k">function</span>(<span class="pl-smi">b</span>,<span class="pl-smi">e</span>){<span class="pl-k">return</span>(<span class="pl-k">new</span> <span class="pl-en">n.HMAC.init</span>(a,</td>
      </tr>
      <tr>
        <td id="L13" class="blob-num js-line-number" data-line-number="13"></td>
        <td id="LC13" class="blob-code blob-code-inner js-file-line">e)).<span class="pl-en">finalize</span>(b)}}});<span class="pl-k">var</span> n<span class="pl-k">=</span><span class="pl-smi">d</span>.<span class="pl-smi">algo</span><span class="pl-k">=</span>{};<span class="pl-k">return</span> d}(<span class="pl-c1">Math</span>);</td>
      </tr>
      <tr>
        <td id="L14" class="blob-num js-line-number" data-line-number="14"></td>
        <td id="LC14" class="blob-code blob-code-inner js-file-line">(<span class="pl-k">function</span>(){<span class="pl-k">var</span> u<span class="pl-k">=</span>CryptoJS,p<span class="pl-k">=</span><span class="pl-smi">u</span>.<span class="pl-smi">lib</span>.<span class="pl-smi">WordArray</span>;<span class="pl-smi">u</span>.<span class="pl-smi">enc</span>.<span class="pl-smi">Base64</span><span class="pl-k">=</span>{<span class="pl-en">stringify</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">d</span>){<span class="pl-k">var</span> l<span class="pl-k">=</span><span class="pl-smi">d</span>.<span class="pl-smi">words</span>,p<span class="pl-k">=</span><span class="pl-smi">d</span>.<span class="pl-smi">sigBytes</span>,t<span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-smi">_map</span>;<span class="pl-smi">d</span>.<span class="pl-en">clamp</span>();d<span class="pl-k">=</span>[];<span class="pl-k">for</span>(<span class="pl-k">var</span> r<span class="pl-k">=</span><span class="pl-c1">0</span>;r<span class="pl-k">&lt;</span>p;r<span class="pl-k">+=</span><span class="pl-c1">3</span>)<span class="pl-k">for</span>(<span class="pl-k">var</span> w<span class="pl-k">=</span>(l[r<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">2</span>]<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">24</span><span class="pl-k">-</span><span class="pl-c1">8</span><span class="pl-k">*</span>(r<span class="pl-k">%</span><span class="pl-c1">4</span>)<span class="pl-k">&amp;</span><span class="pl-c1">255</span>)<span class="pl-k">&lt;&lt;</span><span class="pl-c1">16</span><span class="pl-k">|</span>(l[r<span class="pl-k">+</span><span class="pl-c1">1</span><span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">2</span>]<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">24</span><span class="pl-k">-</span><span class="pl-c1">8</span><span class="pl-k">*</span>((r<span class="pl-k">+</span><span class="pl-c1">1</span>)<span class="pl-k">%</span><span class="pl-c1">4</span>)<span class="pl-k">&amp;</span><span class="pl-c1">255</span>)<span class="pl-k">&lt;&lt;</span><span class="pl-c1">8</span><span class="pl-k">|</span>l[r<span class="pl-k">+</span><span class="pl-c1">2</span><span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">2</span>]<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">24</span><span class="pl-k">-</span><span class="pl-c1">8</span><span class="pl-k">*</span>((r<span class="pl-k">+</span><span class="pl-c1">2</span>)<span class="pl-k">%</span><span class="pl-c1">4</span>)<span class="pl-k">&amp;</span><span class="pl-c1">255</span>,v<span class="pl-k">=</span><span class="pl-c1">0</span>;<span class="pl-c1">4</span><span class="pl-k">&gt;</span>v<span class="pl-k">&amp;&amp;</span>r<span class="pl-k">+</span><span class="pl-c1">0.75</span><span class="pl-k">*</span>v<span class="pl-k">&lt;</span>p;v<span class="pl-k">++</span>)<span class="pl-smi">d</span>.<span class="pl-c1">push</span>(<span class="pl-smi">t</span>.<span class="pl-c1">charAt</span>(w<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">6</span><span class="pl-k">*</span>(<span class="pl-c1">3</span><span class="pl-k">-</span>v)<span class="pl-k">&amp;</span><span class="pl-c1">63</span>));<span class="pl-k">if</span>(l<span class="pl-k">=</span><span class="pl-smi">t</span>.<span class="pl-c1">charAt</span>(<span class="pl-c1">64</span>))<span class="pl-k">for</span>(;<span class="pl-smi">d</span>.<span class="pl-c1">length</span><span class="pl-k">%</span><span class="pl-c1">4</span>;)<span class="pl-smi">d</span>.<span class="pl-c1">push</span>(l);<span class="pl-k">return</span> <span class="pl-smi">d</span>.<span class="pl-c1">join</span>(<span class="pl-s"><span class="pl-pds">&quot;</span><span class="pl-pds">&quot;</span></span>)},<span class="pl-en">parse</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">d</span>){<span class="pl-k">var</span> l<span class="pl-k">=</span><span class="pl-smi">d</span>.<span class="pl-c1">length</span>,s<span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-smi">_map</span>,t<span class="pl-k">=</span><span class="pl-smi">s</span>.<span class="pl-c1">charAt</span>(<span class="pl-c1">64</span>);t<span class="pl-k">&amp;&amp;</span>(t<span class="pl-k">=</span><span class="pl-smi">d</span>.<span class="pl-c1">indexOf</span>(t),<span class="pl-k">-</span><span class="pl-c1">1</span><span class="pl-k">!=</span>t<span class="pl-k">&amp;&amp;</span>(l<span class="pl-k">=</span>t));<span class="pl-k">for</span>(<span class="pl-k">var</span> t<span class="pl-k">=</span>[],r<span class="pl-k">=</span><span class="pl-c1">0</span>,w<span class="pl-k">=</span><span class="pl-c1">0</span>;w<span class="pl-k">&lt;</span></td>
      </tr>
      <tr>
        <td id="L15" class="blob-num js-line-number" data-line-number="15"></td>
        <td id="LC15" class="blob-code blob-code-inner js-file-line">l;w<span class="pl-k">++</span>)<span class="pl-k">if</span>(w<span class="pl-k">%</span><span class="pl-c1">4</span>){<span class="pl-k">var</span> v<span class="pl-k">=</span><span class="pl-smi">s</span>.<span class="pl-c1">indexOf</span>(<span class="pl-smi">d</span>.<span class="pl-c1">charAt</span>(w<span class="pl-k">-</span><span class="pl-c1">1</span>))<span class="pl-k">&lt;&lt;</span><span class="pl-c1">2</span><span class="pl-k">*</span>(w<span class="pl-k">%</span><span class="pl-c1">4</span>),b<span class="pl-k">=</span><span class="pl-smi">s</span>.<span class="pl-c1">indexOf</span>(<span class="pl-smi">d</span>.<span class="pl-c1">charAt</span>(w))<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">6</span><span class="pl-k">-</span><span class="pl-c1">2</span><span class="pl-k">*</span>(w<span class="pl-k">%</span><span class="pl-c1">4</span>);t[r<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">2</span>]<span class="pl-k">|=</span>(v<span class="pl-k">|</span>b)<span class="pl-k">&lt;&lt;</span><span class="pl-c1">24</span><span class="pl-k">-</span><span class="pl-c1">8</span><span class="pl-k">*</span>(r<span class="pl-k">%</span><span class="pl-c1">4</span>);r<span class="pl-k">++</span>}<span class="pl-k">return</span> <span class="pl-smi">p</span>.<span class="pl-en">create</span>(t,r)},_map<span class="pl-k">:</span><span class="pl-s"><span class="pl-pds">&quot;</span>ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=<span class="pl-pds">&quot;</span></span>}})();</td>
      </tr>
      <tr>
        <td id="L16" class="blob-num js-line-number" data-line-number="16"></td>
        <td id="LC16" class="blob-code blob-code-inner js-file-line">(<span class="pl-k">function</span>(<span class="pl-smi">u</span>){<span class="pl-k">function</span> <span class="pl-en">p</span>(<span class="pl-smi">b</span>,<span class="pl-smi">n</span>,<span class="pl-smi">a</span>,<span class="pl-smi">c</span>,<span class="pl-smi">e</span>,<span class="pl-smi">j</span>,<span class="pl-smi">k</span>){b<span class="pl-k">=</span>b<span class="pl-k">+</span>(n<span class="pl-k">&amp;</span>a<span class="pl-k">|~</span>n<span class="pl-k">&amp;</span>c)<span class="pl-k">+</span>e<span class="pl-k">+</span>k;<span class="pl-k">return</span>(b<span class="pl-k">&lt;&lt;</span>j<span class="pl-k">|</span>b<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">32</span><span class="pl-k">-</span>j)<span class="pl-k">+</span>n}function d(b,n,a,c,e,j,k){b=b+(n&amp;c|a&amp;~c)+e+k;return(b&lt;&lt;j|b&gt;&gt;&gt;32-j)+n}function l(b,n,a,c,e,j,k){b=b+(n^a^c)+e+k;return(b&lt;&lt;j|b&gt;&gt;&gt;32-j)+n}function s(b,n,a,c,e,j,k){b=b+(a^(n|~c))+e+k;return(b&lt;&lt;j|b&gt;&gt;&gt;32-j)+n}for(var t=CryptoJS,r=t.lib,w=r.WordArray,v=r.Hasher,r=t.algo,b=[],x=0;64&gt;x;x++)b[x]=4294967296*u.abs(u.sin(x+1))|0;r=r.MD5=v.extend({_doReset:function(){this._hash=new w.init([1732584193,4023233417,2562383102,271733878])},</td>
      </tr>
      <tr>
        <td id="L17" class="blob-num js-line-number" data-line-number="17"></td>
        <td id="LC17" class="blob-code blob-code-inner js-file-line"><span class="pl-en">_doProcessBlock</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">q</span>,<span class="pl-smi">n</span>){<span class="pl-k">for</span>(<span class="pl-k">var</span> a<span class="pl-k">=</span><span class="pl-c1">0</span>;<span class="pl-c1">16</span><span class="pl-k">&gt;</span>a;a<span class="pl-k">++</span>){<span class="pl-k">var</span> c<span class="pl-k">=</span>n<span class="pl-k">+</span>a,e<span class="pl-k">=</span>q[c];q[c]<span class="pl-k">=</span>(e<span class="pl-k">&lt;&lt;</span><span class="pl-c1">8</span><span class="pl-k">|</span>e<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">24</span>)<span class="pl-k">&amp;</span><span class="pl-c1">16711935</span><span class="pl-k">|</span>(e<span class="pl-k">&lt;&lt;</span><span class="pl-c1">24</span><span class="pl-k">|</span>e<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">8</span>)<span class="pl-k">&amp;</span><span class="pl-c1">4278255360</span>}<span class="pl-k">var</span> a<span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-smi">_hash</span>.<span class="pl-smi">words</span>,c<span class="pl-k">=</span>q[n<span class="pl-k">+</span><span class="pl-c1">0</span>],e<span class="pl-k">=</span>q[n<span class="pl-k">+</span><span class="pl-c1">1</span>],j<span class="pl-k">=</span>q[n<span class="pl-k">+</span><span class="pl-c1">2</span>],k<span class="pl-k">=</span>q[n<span class="pl-k">+</span><span class="pl-c1">3</span>],z<span class="pl-k">=</span>q[n<span class="pl-k">+</span><span class="pl-c1">4</span>],r<span class="pl-k">=</span>q[n<span class="pl-k">+</span><span class="pl-c1">5</span>],t<span class="pl-k">=</span>q[n<span class="pl-k">+</span><span class="pl-c1">6</span>],w<span class="pl-k">=</span>q[n<span class="pl-k">+</span><span class="pl-c1">7</span>],v<span class="pl-k">=</span>q[n<span class="pl-k">+</span><span class="pl-c1">8</span>],<span class="pl-c1">A</span><span class="pl-k">=</span>q[n<span class="pl-k">+</span><span class="pl-c1">9</span>],<span class="pl-c1">B</span><span class="pl-k">=</span>q[n<span class="pl-k">+</span><span class="pl-c1">10</span>],<span class="pl-c1">C</span><span class="pl-k">=</span>q[n<span class="pl-k">+</span><span class="pl-c1">11</span>],u<span class="pl-k">=</span>q[n<span class="pl-k">+</span><span class="pl-c1">12</span>],<span class="pl-c1">D</span><span class="pl-k">=</span>q[n<span class="pl-k">+</span><span class="pl-c1">13</span>],<span class="pl-c1">E</span><span class="pl-k">=</span>q[n<span class="pl-k">+</span><span class="pl-c1">14</span>],x<span class="pl-k">=</span>q[n<span class="pl-k">+</span><span class="pl-c1">15</span>],f<span class="pl-k">=</span>a[<span class="pl-c1">0</span>],m<span class="pl-k">=</span>a[<span class="pl-c1">1</span>],g<span class="pl-k">=</span>a[<span class="pl-c1">2</span>],h<span class="pl-k">=</span>a[<span class="pl-c1">3</span>],f<span class="pl-k">=</span><span class="pl-en">p</span>(f,m,g,h,c,<span class="pl-c1">7</span>,b[<span class="pl-c1">0</span>]),h<span class="pl-k">=</span><span class="pl-en">p</span>(h,f,m,g,e,<span class="pl-c1">12</span>,b[<span class="pl-c1">1</span>]),g<span class="pl-k">=</span><span class="pl-en">p</span>(g,h,f,m,j,<span class="pl-c1">17</span>,b[<span class="pl-c1">2</span>]),m<span class="pl-k">=</span><span class="pl-en">p</span>(m,g,h,f,k,<span class="pl-c1">22</span>,b[<span class="pl-c1">3</span>]),f<span class="pl-k">=</span><span class="pl-en">p</span>(f,m,g,h,z,<span class="pl-c1">7</span>,b[<span class="pl-c1">4</span>]),h<span class="pl-k">=</span><span class="pl-en">p</span>(h,f,m,g,r,<span class="pl-c1">12</span>,b[<span class="pl-c1">5</span>]),g<span class="pl-k">=</span><span class="pl-en">p</span>(g,h,f,m,t,<span class="pl-c1">17</span>,b[<span class="pl-c1">6</span>]),m<span class="pl-k">=</span><span class="pl-en">p</span>(m,g,h,f,w,<span class="pl-c1">22</span>,b[<span class="pl-c1">7</span>]),</td>
      </tr>
      <tr>
        <td id="L18" class="blob-num js-line-number" data-line-number="18"></td>
        <td id="LC18" class="blob-code blob-code-inner js-file-line">f<span class="pl-k">=</span><span class="pl-en">p</span>(f,m,g,h,v,<span class="pl-c1">7</span>,b[<span class="pl-c1">8</span>]),h<span class="pl-k">=</span><span class="pl-en">p</span>(h,f,m,g,<span class="pl-c1">A</span>,<span class="pl-c1">12</span>,b[<span class="pl-c1">9</span>]),g<span class="pl-k">=</span><span class="pl-en">p</span>(g,h,f,m,<span class="pl-c1">B</span>,<span class="pl-c1">17</span>,b[<span class="pl-c1">10</span>]),m<span class="pl-k">=</span><span class="pl-en">p</span>(m,g,h,f,<span class="pl-c1">C</span>,<span class="pl-c1">22</span>,b[<span class="pl-c1">11</span>]),f<span class="pl-k">=</span><span class="pl-en">p</span>(f,m,g,h,u,<span class="pl-c1">7</span>,b[<span class="pl-c1">12</span>]),h<span class="pl-k">=</span><span class="pl-en">p</span>(h,f,m,g,<span class="pl-c1">D</span>,<span class="pl-c1">12</span>,b[<span class="pl-c1">13</span>]),g<span class="pl-k">=</span><span class="pl-en">p</span>(g,h,f,m,<span class="pl-c1">E</span>,<span class="pl-c1">17</span>,b[<span class="pl-c1">14</span>]),m<span class="pl-k">=</span><span class="pl-en">p</span>(m,g,h,f,x,<span class="pl-c1">22</span>,b[<span class="pl-c1">15</span>]),f<span class="pl-k">=</span><span class="pl-en">d</span>(f,m,g,h,e,<span class="pl-c1">5</span>,b[<span class="pl-c1">16</span>]),h<span class="pl-k">=</span><span class="pl-en">d</span>(h,f,m,g,t,<span class="pl-c1">9</span>,b[<span class="pl-c1">17</span>]),g<span class="pl-k">=</span><span class="pl-en">d</span>(g,h,f,m,<span class="pl-c1">C</span>,<span class="pl-c1">14</span>,b[<span class="pl-c1">18</span>]),m<span class="pl-k">=</span><span class="pl-en">d</span>(m,g,h,f,c,<span class="pl-c1">20</span>,b[<span class="pl-c1">19</span>]),f<span class="pl-k">=</span><span class="pl-en">d</span>(f,m,g,h,r,<span class="pl-c1">5</span>,b[<span class="pl-c1">20</span>]),h<span class="pl-k">=</span><span class="pl-en">d</span>(h,f,m,g,<span class="pl-c1">B</span>,<span class="pl-c1">9</span>,b[<span class="pl-c1">21</span>]),g<span class="pl-k">=</span><span class="pl-en">d</span>(g,h,f,m,x,<span class="pl-c1">14</span>,b[<span class="pl-c1">22</span>]),m<span class="pl-k">=</span><span class="pl-en">d</span>(m,g,h,f,z,<span class="pl-c1">20</span>,b[<span class="pl-c1">23</span>]),f<span class="pl-k">=</span><span class="pl-en">d</span>(f,m,g,h,<span class="pl-c1">A</span>,<span class="pl-c1">5</span>,b[<span class="pl-c1">24</span>]),h<span class="pl-k">=</span><span class="pl-en">d</span>(h,f,m,g,<span class="pl-c1">E</span>,<span class="pl-c1">9</span>,b[<span class="pl-c1">25</span>]),g<span class="pl-k">=</span><span class="pl-en">d</span>(g,h,f,m,k,<span class="pl-c1">14</span>,b[<span class="pl-c1">26</span>]),m<span class="pl-k">=</span><span class="pl-en">d</span>(m,g,h,f,v,<span class="pl-c1">20</span>,b[<span class="pl-c1">27</span>]),f<span class="pl-k">=</span><span class="pl-en">d</span>(f,m,g,h,<span class="pl-c1">D</span>,<span class="pl-c1">5</span>,b[<span class="pl-c1">28</span>]),h<span class="pl-k">=</span><span class="pl-en">d</span>(h,f,</td>
      </tr>
      <tr>
        <td id="L19" class="blob-num js-line-number" data-line-number="19"></td>
        <td id="LC19" class="blob-code blob-code-inner js-file-line">m,g,j,<span class="pl-c1">9</span>,b[<span class="pl-c1">29</span>]),g<span class="pl-k">=</span><span class="pl-en">d</span>(g,h,f,m,w,<span class="pl-c1">14</span>,b[<span class="pl-c1">30</span>]),m<span class="pl-k">=</span><span class="pl-en">d</span>(m,g,h,f,u,<span class="pl-c1">20</span>,b[<span class="pl-c1">31</span>]),f<span class="pl-k">=</span><span class="pl-en">l</span>(f,m,g,h,r,<span class="pl-c1">4</span>,b[<span class="pl-c1">32</span>]),h<span class="pl-k">=</span><span class="pl-en">l</span>(h,f,m,g,v,<span class="pl-c1">11</span>,b[<span class="pl-c1">33</span>]),g<span class="pl-k">=</span><span class="pl-en">l</span>(g,h,f,m,<span class="pl-c1">C</span>,<span class="pl-c1">16</span>,b[<span class="pl-c1">34</span>]),m<span class="pl-k">=</span><span class="pl-en">l</span>(m,g,h,f,<span class="pl-c1">E</span>,<span class="pl-c1">23</span>,b[<span class="pl-c1">35</span>]),f<span class="pl-k">=</span><span class="pl-en">l</span>(f,m,g,h,e,<span class="pl-c1">4</span>,b[<span class="pl-c1">36</span>]),h<span class="pl-k">=</span><span class="pl-en">l</span>(h,f,m,g,z,<span class="pl-c1">11</span>,b[<span class="pl-c1">37</span>]),g<span class="pl-k">=</span><span class="pl-en">l</span>(g,h,f,m,w,<span class="pl-c1">16</span>,b[<span class="pl-c1">38</span>]),m<span class="pl-k">=</span><span class="pl-en">l</span>(m,g,h,f,<span class="pl-c1">B</span>,<span class="pl-c1">23</span>,b[<span class="pl-c1">39</span>]),f<span class="pl-k">=</span><span class="pl-en">l</span>(f,m,g,h,<span class="pl-c1">D</span>,<span class="pl-c1">4</span>,b[<span class="pl-c1">40</span>]),h<span class="pl-k">=</span><span class="pl-en">l</span>(h,f,m,g,c,<span class="pl-c1">11</span>,b[<span class="pl-c1">41</span>]),g<span class="pl-k">=</span><span class="pl-en">l</span>(g,h,f,m,k,<span class="pl-c1">16</span>,b[<span class="pl-c1">42</span>]),m<span class="pl-k">=</span><span class="pl-en">l</span>(m,g,h,f,t,<span class="pl-c1">23</span>,b[<span class="pl-c1">43</span>]),f<span class="pl-k">=</span><span class="pl-en">l</span>(f,m,g,h,<span class="pl-c1">A</span>,<span class="pl-c1">4</span>,b[<span class="pl-c1">44</span>]),h<span class="pl-k">=</span><span class="pl-en">l</span>(h,f,m,g,u,<span class="pl-c1">11</span>,b[<span class="pl-c1">45</span>]),g<span class="pl-k">=</span><span class="pl-en">l</span>(g,h,f,m,x,<span class="pl-c1">16</span>,b[<span class="pl-c1">46</span>]),m<span class="pl-k">=</span><span class="pl-en">l</span>(m,g,h,f,j,<span class="pl-c1">23</span>,b[<span class="pl-c1">47</span>]),f<span class="pl-k">=</span><span class="pl-en">s</span>(f,m,g,h,c,<span class="pl-c1">6</span>,b[<span class="pl-c1">48</span>]),h<span class="pl-k">=</span><span class="pl-en">s</span>(h,f,m,g,w,<span class="pl-c1">10</span>,b[<span class="pl-c1">49</span>]),g<span class="pl-k">=</span><span class="pl-en">s</span>(g,h,f,m,</td>
      </tr>
      <tr>
        <td id="L20" class="blob-num js-line-number" data-line-number="20"></td>
        <td id="LC20" class="blob-code blob-code-inner js-file-line"><span class="pl-c1">E</span>,<span class="pl-c1">15</span>,b[<span class="pl-c1">50</span>]),m<span class="pl-k">=</span><span class="pl-en">s</span>(m,g,h,f,r,<span class="pl-c1">21</span>,b[<span class="pl-c1">51</span>]),f<span class="pl-k">=</span><span class="pl-en">s</span>(f,m,g,h,u,<span class="pl-c1">6</span>,b[<span class="pl-c1">52</span>]),h<span class="pl-k">=</span><span class="pl-en">s</span>(h,f,m,g,k,<span class="pl-c1">10</span>,b[<span class="pl-c1">53</span>]),g<span class="pl-k">=</span><span class="pl-en">s</span>(g,h,f,m,<span class="pl-c1">B</span>,<span class="pl-c1">15</span>,b[<span class="pl-c1">54</span>]),m<span class="pl-k">=</span><span class="pl-en">s</span>(m,g,h,f,e,<span class="pl-c1">21</span>,b[<span class="pl-c1">55</span>]),f<span class="pl-k">=</span><span class="pl-en">s</span>(f,m,g,h,v,<span class="pl-c1">6</span>,b[<span class="pl-c1">56</span>]),h<span class="pl-k">=</span><span class="pl-en">s</span>(h,f,m,g,x,<span class="pl-c1">10</span>,b[<span class="pl-c1">57</span>]),g<span class="pl-k">=</span><span class="pl-en">s</span>(g,h,f,m,t,<span class="pl-c1">15</span>,b[<span class="pl-c1">58</span>]),m<span class="pl-k">=</span><span class="pl-en">s</span>(m,g,h,f,<span class="pl-c1">D</span>,<span class="pl-c1">21</span>,b[<span class="pl-c1">59</span>]),f<span class="pl-k">=</span><span class="pl-en">s</span>(f,m,g,h,z,<span class="pl-c1">6</span>,b[<span class="pl-c1">60</span>]),h<span class="pl-k">=</span><span class="pl-en">s</span>(h,f,m,g,<span class="pl-c1">C</span>,<span class="pl-c1">10</span>,b[<span class="pl-c1">61</span>]),g<span class="pl-k">=</span><span class="pl-en">s</span>(g,h,f,m,j,<span class="pl-c1">15</span>,b[<span class="pl-c1">62</span>]),m<span class="pl-k">=</span><span class="pl-en">s</span>(m,g,h,f,<span class="pl-c1">A</span>,<span class="pl-c1">21</span>,b[<span class="pl-c1">63</span>]);a[<span class="pl-c1">0</span>]<span class="pl-k">=</span>a[<span class="pl-c1">0</span>]<span class="pl-k">+</span>f<span class="pl-k">|</span><span class="pl-c1">0</span>;a[<span class="pl-c1">1</span>]<span class="pl-k">=</span>a[<span class="pl-c1">1</span>]<span class="pl-k">+</span>m<span class="pl-k">|</span><span class="pl-c1">0</span>;a[<span class="pl-c1">2</span>]<span class="pl-k">=</span>a[<span class="pl-c1">2</span>]<span class="pl-k">+</span>g<span class="pl-k">|</span><span class="pl-c1">0</span>;a[<span class="pl-c1">3</span>]<span class="pl-k">=</span>a[<span class="pl-c1">3</span>]<span class="pl-k">+</span>h<span class="pl-k">|</span><span class="pl-c1">0</span>},<span class="pl-en">_doFinalize</span><span class="pl-k">:</span><span class="pl-k">function</span>(){<span class="pl-k">var</span> b<span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-smi">_data</span>,n<span class="pl-k">=</span><span class="pl-smi">b</span>.<span class="pl-smi">words</span>,a<span class="pl-k">=</span><span class="pl-c1">8</span><span class="pl-k">*</span><span class="pl-c1">this</span>.<span class="pl-smi">_nDataBytes</span>,c<span class="pl-k">=</span><span class="pl-c1">8</span><span class="pl-k">*</span><span class="pl-smi">b</span>.<span class="pl-smi">sigBytes</span>;n[c<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">5</span>]<span class="pl-k">|=</span><span class="pl-c1">128</span><span class="pl-k">&lt;&lt;</span><span class="pl-c1">24</span><span class="pl-k">-</span>c<span class="pl-k">%</span><span class="pl-c1">32</span>;<span class="pl-k">var</span> e<span class="pl-k">=</span><span class="pl-smi">u</span>.<span class="pl-en">floor</span>(a<span class="pl-k">/</span></td>
      </tr>
      <tr>
        <td id="L21" class="blob-num js-line-number" data-line-number="21"></td>
        <td id="LC21" class="blob-code blob-code-inner js-file-line"><span class="pl-c1">4294967296</span>);n[(c<span class="pl-k">+</span><span class="pl-c1">64</span><span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">9</span><span class="pl-k">&lt;&lt;</span><span class="pl-c1">4</span>)<span class="pl-k">+</span><span class="pl-c1">15</span>]<span class="pl-k">=</span>(e<span class="pl-k">&lt;&lt;</span><span class="pl-c1">8</span><span class="pl-k">|</span>e<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">24</span>)<span class="pl-k">&amp;</span><span class="pl-c1">16711935</span><span class="pl-k">|</span>(e<span class="pl-k">&lt;&lt;</span><span class="pl-c1">24</span><span class="pl-k">|</span>e<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">8</span>)<span class="pl-k">&amp;</span><span class="pl-c1">4278255360</span>;n[(c<span class="pl-k">+</span><span class="pl-c1">64</span><span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">9</span><span class="pl-k">&lt;&lt;</span><span class="pl-c1">4</span>)<span class="pl-k">+</span><span class="pl-c1">14</span>]<span class="pl-k">=</span>(a<span class="pl-k">&lt;&lt;</span><span class="pl-c1">8</span><span class="pl-k">|</span>a<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">24</span>)<span class="pl-k">&amp;</span><span class="pl-c1">16711935</span><span class="pl-k">|</span>(a<span class="pl-k">&lt;&lt;</span><span class="pl-c1">24</span><span class="pl-k">|</span>a<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">8</span>)<span class="pl-k">&amp;</span><span class="pl-c1">4278255360</span>;<span class="pl-smi">b</span>.<span class="pl-smi">sigBytes</span><span class="pl-k">=</span><span class="pl-c1">4</span><span class="pl-k">*</span>(<span class="pl-smi">n</span>.<span class="pl-c1">length</span><span class="pl-k">+</span><span class="pl-c1">1</span>);<span class="pl-c1">this</span>.<span class="pl-en">_process</span>();b<span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-smi">_hash</span>;n<span class="pl-k">=</span><span class="pl-smi">b</span>.<span class="pl-smi">words</span>;<span class="pl-k">for</span>(a<span class="pl-k">=</span><span class="pl-c1">0</span>;<span class="pl-c1">4</span><span class="pl-k">&gt;</span>a;a<span class="pl-k">++</span>)c<span class="pl-k">=</span>n[a],n[a]<span class="pl-k">=</span>(c<span class="pl-k">&lt;&lt;</span><span class="pl-c1">8</span><span class="pl-k">|</span>c<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">24</span>)<span class="pl-k">&amp;</span><span class="pl-c1">16711935</span><span class="pl-k">|</span>(c<span class="pl-k">&lt;&lt;</span><span class="pl-c1">24</span><span class="pl-k">|</span>c<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">8</span>)<span class="pl-k">&amp;</span><span class="pl-c1">4278255360</span>;<span class="pl-k">return</span> b},<span class="pl-en">clone</span><span class="pl-k">:</span><span class="pl-k">function</span>(){<span class="pl-k">var</span> b<span class="pl-k">=</span><span class="pl-smi">v</span>.<span class="pl-smi">clone</span>.<span class="pl-c1">call</span>(<span class="pl-c1">this</span>);<span class="pl-smi">b</span>.<span class="pl-smi">_hash</span><span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-smi">_hash</span>.<span class="pl-en">clone</span>();<span class="pl-k">return</span> b}});<span class="pl-smi">t</span>.<span class="pl-c1">MD5</span><span class="pl-k">=</span><span class="pl-smi">v</span>.<span class="pl-en">_createHelper</span>(r);<span class="pl-smi">t</span>.<span class="pl-smi">HmacMD5</span><span class="pl-k">=</span><span class="pl-smi">v</span>.<span class="pl-en">_createHmacHelper</span>(r)})(<span class="pl-c1">Math</span>);</td>
      </tr>
      <tr>
        <td id="L22" class="blob-num js-line-number" data-line-number="22"></td>
        <td id="LC22" class="blob-code blob-code-inner js-file-line">(<span class="pl-k">function</span>(){<span class="pl-k">var</span> u<span class="pl-k">=</span>CryptoJS,p<span class="pl-k">=</span><span class="pl-smi">u</span>.<span class="pl-smi">lib</span>,d<span class="pl-k">=</span><span class="pl-smi">p</span>.<span class="pl-smi">Base</span>,l<span class="pl-k">=</span><span class="pl-smi">p</span>.<span class="pl-smi">WordArray</span>,p<span class="pl-k">=</span><span class="pl-smi">u</span>.<span class="pl-smi">algo</span>,s<span class="pl-k">=</span><span class="pl-smi">p</span>.<span class="pl-smi">EvpKDF</span><span class="pl-k">=</span><span class="pl-smi">d</span>.<span class="pl-en">extend</span>({cfg<span class="pl-k">:</span><span class="pl-smi">d</span>.<span class="pl-en">extend</span>({keySize<span class="pl-k">:</span><span class="pl-c1">4</span>,hasher<span class="pl-k">:</span><span class="pl-smi">p</span>.<span class="pl-c1">MD5</span>,iterations<span class="pl-k">:</span><span class="pl-c1">1</span>}),<span class="pl-en">init</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">d</span>){<span class="pl-c1">this</span>.<span class="pl-smi">cfg</span><span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-smi">cfg</span>.<span class="pl-en">extend</span>(d)},<span class="pl-en">compute</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">d</span>,<span class="pl-smi">r</span>){<span class="pl-k">for</span>(<span class="pl-k">var</span> p<span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-smi">cfg</span>,s<span class="pl-k">=</span><span class="pl-smi">p</span>.<span class="pl-smi">hasher</span>.<span class="pl-en">create</span>(),b<span class="pl-k">=</span><span class="pl-smi">l</span>.<span class="pl-en">create</span>(),u<span class="pl-k">=</span><span class="pl-smi">b</span>.<span class="pl-smi">words</span>,q<span class="pl-k">=</span><span class="pl-smi">p</span>.<span class="pl-smi">keySize</span>,p<span class="pl-k">=</span><span class="pl-smi">p</span>.<span class="pl-smi">iterations</span>;<span class="pl-smi">u</span>.<span class="pl-c1">length</span><span class="pl-k">&lt;</span>q;){n<span class="pl-k">&amp;&amp;</span><span class="pl-smi">s</span>.<span class="pl-en">update</span>(n);<span class="pl-k">var</span> n<span class="pl-k">=</span><span class="pl-smi">s</span>.<span class="pl-en">update</span>(d).<span class="pl-en">finalize</span>(r);<span class="pl-smi">s</span>.<span class="pl-c1">reset</span>();<span class="pl-k">for</span>(<span class="pl-k">var</span> a<span class="pl-k">=</span><span class="pl-c1">1</span>;a<span class="pl-k">&lt;</span>p;a<span class="pl-k">++</span>)n<span class="pl-k">=</span><span class="pl-smi">s</span>.<span class="pl-en">finalize</span>(n),<span class="pl-smi">s</span>.<span class="pl-c1">reset</span>();<span class="pl-smi">b</span>.<span class="pl-c1">concat</span>(n)}<span class="pl-smi">b</span>.<span class="pl-smi">sigBytes</span><span class="pl-k">=</span><span class="pl-c1">4</span><span class="pl-k">*</span>q;<span class="pl-k">return</span> b}});<span class="pl-smi">u</span>.<span class="pl-en">EvpKDF</span><span class="pl-k">=</span><span class="pl-k">function</span>(<span class="pl-smi">d</span>,<span class="pl-smi">l</span>,<span class="pl-smi">p</span>){<span class="pl-k">return</span> <span class="pl-smi">s</span>.<span class="pl-en">create</span>(p).<span class="pl-en">compute</span>(d,</td>
      </tr>
      <tr>
        <td id="L23" class="blob-num js-line-number" data-line-number="23"></td>
        <td id="LC23" class="blob-code blob-code-inner js-file-line">l)}})();</td>
      </tr>
      <tr>
        <td id="L24" class="blob-num js-line-number" data-line-number="24"></td>
        <td id="LC24" class="blob-code blob-code-inner js-file-line"><span class="pl-smi">CryptoJS</span>.<span class="pl-smi">lib</span>.<span class="pl-smi">Cipher</span><span class="pl-k">||</span><span class="pl-k">function</span>(<span class="pl-smi">u</span>){<span class="pl-k">var</span> p<span class="pl-k">=</span>CryptoJS,d<span class="pl-k">=</span><span class="pl-smi">p</span>.<span class="pl-smi">lib</span>,l<span class="pl-k">=</span><span class="pl-smi">d</span>.<span class="pl-smi">Base</span>,s<span class="pl-k">=</span><span class="pl-smi">d</span>.<span class="pl-smi">WordArray</span>,t<span class="pl-k">=</span><span class="pl-smi">d</span>.<span class="pl-smi">BufferedBlockAlgorithm</span>,r<span class="pl-k">=</span><span class="pl-smi">p</span>.<span class="pl-smi">enc</span>.<span class="pl-smi">Base64</span>,w<span class="pl-k">=</span><span class="pl-smi">p</span>.<span class="pl-smi">algo</span>.<span class="pl-smi">EvpKDF</span>,v<span class="pl-k">=</span><span class="pl-smi">d</span>.<span class="pl-smi">Cipher</span><span class="pl-k">=</span><span class="pl-smi">t</span>.<span class="pl-en">extend</span>({cfg<span class="pl-k">:</span><span class="pl-smi">l</span>.<span class="pl-en">extend</span>(),<span class="pl-en">createEncryptor</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">e</span>,<span class="pl-smi">a</span>){<span class="pl-k">return</span> <span class="pl-c1">this</span>.<span class="pl-en">create</span>(<span class="pl-c1">this</span>.<span class="pl-smi">_ENC_XFORM_MODE</span>,e,a)},<span class="pl-en">createDecryptor</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">e</span>,<span class="pl-smi">a</span>){<span class="pl-k">return</span> <span class="pl-c1">this</span>.<span class="pl-en">create</span>(<span class="pl-c1">this</span>.<span class="pl-smi">_DEC_XFORM_MODE</span>,e,a)},<span class="pl-en">init</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">e</span>,<span class="pl-smi">a</span>,<span class="pl-smi">b</span>){<span class="pl-c1">this</span>.<span class="pl-smi">cfg</span><span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-smi">cfg</span>.<span class="pl-en">extend</span>(b);<span class="pl-c1">this</span>.<span class="pl-smi">_xformMode</span><span class="pl-k">=</span>e;<span class="pl-c1">this</span>.<span class="pl-smi">_key</span><span class="pl-k">=</span>a;<span class="pl-c1">this</span>.<span class="pl-c1">reset</span>()},<span class="pl-en">reset</span><span class="pl-k">:</span><span class="pl-k">function</span>(){<span class="pl-smi">t</span>.<span class="pl-smi">reset</span>.<span class="pl-c1">call</span>(<span class="pl-c1">this</span>);<span class="pl-c1">this</span>.<span class="pl-en">_doReset</span>()},<span class="pl-en">process</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">e</span>){<span class="pl-c1">this</span>.<span class="pl-en">_append</span>(e);<span class="pl-k">return</span> <span class="pl-c1">this</span>.<span class="pl-en">_process</span>()},</td>
      </tr>
      <tr>
        <td id="L25" class="blob-num js-line-number" data-line-number="25"></td>
        <td id="LC25" class="blob-code blob-code-inner js-file-line"><span class="pl-en">finalize</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">e</span>){e<span class="pl-k">&amp;&amp;</span><span class="pl-c1">this</span>.<span class="pl-en">_append</span>(e);<span class="pl-k">return</span> <span class="pl-c1">this</span>.<span class="pl-en">_doFinalize</span>()},keySize<span class="pl-k">:</span><span class="pl-c1">4</span>,ivSize<span class="pl-k">:</span><span class="pl-c1">4</span>,<span class="pl-c1">_ENC_XFORM_MODE</span><span class="pl-k">:</span><span class="pl-c1">1</span>,<span class="pl-c1">_DEC_XFORM_MODE</span><span class="pl-k">:</span><span class="pl-c1">2</span>,<span class="pl-en">_createHelper</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">e</span>){<span class="pl-k">return</span>{<span class="pl-en">encrypt</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">b</span>,<span class="pl-smi">k</span>,<span class="pl-smi">d</span>){<span class="pl-k">return</span>(<span class="pl-s"><span class="pl-pds">&quot;</span>string<span class="pl-pds">&quot;</span></span><span class="pl-k">==</span><span class="pl-k">typeof</span> k<span class="pl-k">?</span>c<span class="pl-k">:</span>a).<span class="pl-en">encrypt</span>(e,b,k,d)},<span class="pl-en">decrypt</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">b</span>,<span class="pl-smi">k</span>,<span class="pl-smi">d</span>){<span class="pl-k">return</span>(<span class="pl-s"><span class="pl-pds">&quot;</span>string<span class="pl-pds">&quot;</span></span><span class="pl-k">==</span><span class="pl-k">typeof</span> k<span class="pl-k">?</span>c<span class="pl-k">:</span>a).<span class="pl-en">decrypt</span>(e,b,k,d)}}}});<span class="pl-smi">d</span>.<span class="pl-smi">StreamCipher</span><span class="pl-k">=</span><span class="pl-smi">v</span>.<span class="pl-en">extend</span>({<span class="pl-en">_doFinalize</span><span class="pl-k">:</span><span class="pl-k">function</span>(){<span class="pl-k">return</span> <span class="pl-c1">this</span>.<span class="pl-en">_process</span>(<span class="pl-k">!</span><span class="pl-c1">0</span>)},blockSize<span class="pl-k">:</span><span class="pl-c1">1</span>});<span class="pl-k">var</span> b<span class="pl-k">=</span><span class="pl-smi">p</span>.<span class="pl-smi">mode</span><span class="pl-k">=</span>{},<span class="pl-en">x</span><span class="pl-k">=</span><span class="pl-k">function</span>(<span class="pl-smi">e</span>,<span class="pl-smi">a</span>,<span class="pl-smi">b</span>){<span class="pl-k">var</span> c<span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-smi">_iv</span>;c<span class="pl-k">?</span><span class="pl-c1">this</span>.<span class="pl-smi">_iv</span><span class="pl-k">=</span>u<span class="pl-k">:</span>c<span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-smi">_prevBlock</span>;<span class="pl-k">for</span>(<span class="pl-k">var</span> d<span class="pl-k">=</span><span class="pl-c1">0</span>;d<span class="pl-k">&lt;</span>b;d<span class="pl-k">++</span>)e[a<span class="pl-k">+</span>d]<span class="pl-k">^=</span></td>
      </tr>
      <tr>
        <td id="L26" class="blob-num js-line-number" data-line-number="26"></td>
        <td id="LC26" class="blob-code blob-code-inner js-file-line">c[d]},q<span class="pl-k">=</span>(<span class="pl-smi">d</span>.<span class="pl-smi">BlockCipherMode</span><span class="pl-k">=</span><span class="pl-smi">l</span>.<span class="pl-en">extend</span>({<span class="pl-en">createEncryptor</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">e</span>,<span class="pl-smi">a</span>){<span class="pl-k">return</span> <span class="pl-c1">this</span>.<span class="pl-smi">Encryptor</span>.<span class="pl-en">create</span>(e,a)},<span class="pl-en">createDecryptor</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">e</span>,<span class="pl-smi">a</span>){<span class="pl-k">return</span> <span class="pl-c1">this</span>.<span class="pl-smi">Decryptor</span>.<span class="pl-en">create</span>(e,a)},<span class="pl-en">init</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">e</span>,<span class="pl-smi">a</span>){<span class="pl-c1">this</span>.<span class="pl-smi">_cipher</span><span class="pl-k">=</span>e;<span class="pl-c1">this</span>.<span class="pl-smi">_iv</span><span class="pl-k">=</span>a}})).<span class="pl-en">extend</span>();<span class="pl-smi">q</span>.<span class="pl-smi">Encryptor</span><span class="pl-k">=</span><span class="pl-smi">q</span>.<span class="pl-en">extend</span>({<span class="pl-en">processBlock</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">e</span>,<span class="pl-smi">a</span>){<span class="pl-k">var</span> b<span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-smi">_cipher</span>,c<span class="pl-k">=</span><span class="pl-smi">b</span>.<span class="pl-smi">blockSize</span>;<span class="pl-smi">x</span>.<span class="pl-c1">call</span>(<span class="pl-c1">this</span>,e,a,c);<span class="pl-smi">b</span>.<span class="pl-en">encryptBlock</span>(e,a);<span class="pl-c1">this</span>.<span class="pl-smi">_prevBlock</span><span class="pl-k">=</span><span class="pl-smi">e</span>.<span class="pl-c1">slice</span>(a,a<span class="pl-k">+</span>c)}});<span class="pl-smi">q</span>.<span class="pl-smi">Decryptor</span><span class="pl-k">=</span><span class="pl-smi">q</span>.<span class="pl-en">extend</span>({<span class="pl-en">processBlock</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">e</span>,<span class="pl-smi">a</span>){<span class="pl-k">var</span> b<span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-smi">_cipher</span>,c<span class="pl-k">=</span><span class="pl-smi">b</span>.<span class="pl-smi">blockSize</span>,d<span class="pl-k">=</span><span class="pl-smi">e</span>.<span class="pl-c1">slice</span>(a,a<span class="pl-k">+</span>c);<span class="pl-smi">b</span>.<span class="pl-en">decryptBlock</span>(e,a);<span class="pl-smi">x</span>.<span class="pl-c1">call</span>(<span class="pl-c1">this</span>,</td>
      </tr>
      <tr>
        <td id="L27" class="blob-num js-line-number" data-line-number="27"></td>
        <td id="LC27" class="blob-code blob-code-inner js-file-line">e,a,c);<span class="pl-c1">this</span>.<span class="pl-smi">_prevBlock</span><span class="pl-k">=</span>d}});b<span class="pl-k">=</span><span class="pl-smi">b</span>.<span class="pl-c1">CBC</span><span class="pl-k">=</span>q;q<span class="pl-k">=</span>(<span class="pl-smi">p</span>.<span class="pl-smi">pad</span><span class="pl-k">=</span>{}).<span class="pl-smi">Pkcs7</span><span class="pl-k">=</span>{<span class="pl-en">pad</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">a</span>,<span class="pl-smi">b</span>){<span class="pl-k">for</span>(<span class="pl-k">var</span> c<span class="pl-k">=</span><span class="pl-c1">4</span><span class="pl-k">*</span>b,c<span class="pl-k">=</span>c<span class="pl-k">-</span><span class="pl-smi">a</span>.<span class="pl-smi">sigBytes</span><span class="pl-k">%</span>c,d<span class="pl-k">=</span>c<span class="pl-k">&lt;&lt;</span><span class="pl-c1">24</span><span class="pl-k">|</span>c<span class="pl-k">&lt;&lt;</span><span class="pl-c1">16</span><span class="pl-k">|</span>c<span class="pl-k">&lt;&lt;</span><span class="pl-c1">8</span><span class="pl-k">|</span>c,l<span class="pl-k">=</span>[],n<span class="pl-k">=</span><span class="pl-c1">0</span>;n<span class="pl-k">&lt;</span>c;n<span class="pl-k">+=</span><span class="pl-c1">4</span>)<span class="pl-smi">l</span>.<span class="pl-c1">push</span>(d);c<span class="pl-k">=</span><span class="pl-smi">s</span>.<span class="pl-en">create</span>(l,c);<span class="pl-smi">a</span>.<span class="pl-c1">concat</span>(c)},<span class="pl-en">unpad</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">a</span>){<span class="pl-smi">a</span>.<span class="pl-smi">sigBytes</span><span class="pl-k">-=</span><span class="pl-smi">a</span>.<span class="pl-smi">words</span>[<span class="pl-smi">a</span>.<span class="pl-smi">sigBytes</span><span class="pl-k">-</span><span class="pl-c1">1</span><span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">2</span>]<span class="pl-k">&amp;</span><span class="pl-c1">255</span>}};<span class="pl-smi">d</span>.<span class="pl-smi">BlockCipher</span><span class="pl-k">=</span><span class="pl-smi">v</span>.<span class="pl-en">extend</span>({cfg<span class="pl-k">:</span><span class="pl-smi">v</span>.<span class="pl-smi">cfg</span>.<span class="pl-en">extend</span>({mode<span class="pl-k">:</span>b,padding<span class="pl-k">:</span>q}),<span class="pl-en">reset</span><span class="pl-k">:</span><span class="pl-k">function</span>(){<span class="pl-smi">v</span>.<span class="pl-smi">reset</span>.<span class="pl-c1">call</span>(<span class="pl-c1">this</span>);<span class="pl-k">var</span> a<span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-smi">cfg</span>,b<span class="pl-k">=</span><span class="pl-smi">a</span>.<span class="pl-smi">iv</span>,a<span class="pl-k">=</span><span class="pl-smi">a</span>.<span class="pl-smi">mode</span>;<span class="pl-k">if</span>(<span class="pl-c1">this</span>.<span class="pl-smi">_xformMode</span><span class="pl-k">==</span><span class="pl-c1">this</span>.<span class="pl-smi">_ENC_XFORM_MODE</span>)<span class="pl-k">var</span> c<span class="pl-k">=</span><span class="pl-smi">a</span>.<span class="pl-smi">createEncryptor</span>;<span class="pl-k">else</span> c<span class="pl-k">=</span><span class="pl-smi">a</span>.<span class="pl-smi">createDecryptor</span>,<span class="pl-c1">this</span>.<span class="pl-smi">_minBufferSize</span><span class="pl-k">=</span><span class="pl-c1">1</span>;<span class="pl-c1">this</span>.<span class="pl-smi">_mode</span><span class="pl-k">=</span><span class="pl-smi">c</span>.<span class="pl-c1">call</span>(a,</td>
      </tr>
      <tr>
        <td id="L28" class="blob-num js-line-number" data-line-number="28"></td>
        <td id="LC28" class="blob-code blob-code-inner js-file-line"><span class="pl-c1">this</span>,b<span class="pl-k">&amp;&amp;</span><span class="pl-smi">b</span>.<span class="pl-smi">words</span>)},<span class="pl-en">_doProcessBlock</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">a</span>,<span class="pl-smi">b</span>){<span class="pl-c1">this</span>.<span class="pl-smi">_mode</span>.<span class="pl-en">processBlock</span>(a,b)},<span class="pl-en">_doFinalize</span><span class="pl-k">:</span><span class="pl-k">function</span>(){<span class="pl-k">var</span> a<span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-smi">cfg</span>.<span class="pl-smi">padding</span>;<span class="pl-k">if</span>(<span class="pl-c1">this</span>.<span class="pl-smi">_xformMode</span><span class="pl-k">==</span><span class="pl-c1">this</span>.<span class="pl-smi">_ENC_XFORM_MODE</span>){<span class="pl-smi">a</span>.<span class="pl-en">pad</span>(<span class="pl-c1">this</span>.<span class="pl-smi">_data</span>,<span class="pl-c1">this</span>.<span class="pl-smi">blockSize</span>);<span class="pl-k">var</span> b<span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-en">_process</span>(<span class="pl-k">!</span><span class="pl-c1">0</span>)}<span class="pl-k">else</span> b<span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-en">_process</span>(<span class="pl-k">!</span><span class="pl-c1">0</span>),<span class="pl-smi">a</span>.<span class="pl-en">unpad</span>(b);<span class="pl-k">return</span> b},blockSize<span class="pl-k">:</span><span class="pl-c1">4</span>});<span class="pl-k">var</span> n<span class="pl-k">=</span><span class="pl-smi">d</span>.<span class="pl-smi">CipherParams</span><span class="pl-k">=</span><span class="pl-smi">l</span>.<span class="pl-en">extend</span>({<span class="pl-en">init</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">a</span>){<span class="pl-c1">this</span>.<span class="pl-en">mixIn</span>(a)},<span class="pl-en">toString</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">a</span>){<span class="pl-k">return</span>(a<span class="pl-k">||</span><span class="pl-c1">this</span>.<span class="pl-smi">formatter</span>).<span class="pl-en">stringify</span>(<span class="pl-c1">this</span>)}}),b<span class="pl-k">=</span>(<span class="pl-smi">p</span>.<span class="pl-smi">format</span><span class="pl-k">=</span>{}).<span class="pl-smi">OpenSSL</span><span class="pl-k">=</span>{<span class="pl-en">stringify</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">a</span>){<span class="pl-k">var</span> b<span class="pl-k">=</span><span class="pl-smi">a</span>.<span class="pl-smi">ciphertext</span>;a<span class="pl-k">=</span><span class="pl-smi">a</span>.<span class="pl-smi">salt</span>;<span class="pl-k">return</span>(a<span class="pl-k">?</span><span class="pl-smi">s</span>.<span class="pl-en">create</span>([<span class="pl-c1">1398893684</span>,</td>
      </tr>
      <tr>
        <td id="L29" class="blob-num js-line-number" data-line-number="29"></td>
        <td id="LC29" class="blob-code blob-code-inner js-file-line"><span class="pl-c1">1701076831</span>]).<span class="pl-c1">concat</span>(a).<span class="pl-c1">concat</span>(b)<span class="pl-k">:</span>b).<span class="pl-c1">toString</span>(r)},<span class="pl-en">parse</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">a</span>){a<span class="pl-k">=</span><span class="pl-smi">r</span>.<span class="pl-c1">parse</span>(a);<span class="pl-k">var</span> b<span class="pl-k">=</span><span class="pl-smi">a</span>.<span class="pl-smi">words</span>;<span class="pl-k">if</span>(<span class="pl-c1">1398893684</span><span class="pl-k">==</span>b[<span class="pl-c1">0</span>]<span class="pl-k">&amp;&amp;</span><span class="pl-c1">1701076831</span><span class="pl-k">==</span>b[<span class="pl-c1">1</span>]){<span class="pl-k">var</span> c<span class="pl-k">=</span><span class="pl-smi">s</span>.<span class="pl-en">create</span>(<span class="pl-smi">b</span>.<span class="pl-c1">slice</span>(<span class="pl-c1">2</span>,<span class="pl-c1">4</span>));<span class="pl-smi">b</span>.<span class="pl-c1">splice</span>(<span class="pl-c1">0</span>,<span class="pl-c1">4</span>);<span class="pl-smi">a</span>.<span class="pl-smi">sigBytes</span><span class="pl-k">-=</span><span class="pl-c1">16</span>}<span class="pl-k">return</span> <span class="pl-smi">n</span>.<span class="pl-en">create</span>({ciphertext<span class="pl-k">:</span>a,salt<span class="pl-k">:</span>c})}},a<span class="pl-k">=</span><span class="pl-smi">d</span>.<span class="pl-smi">SerializableCipher</span><span class="pl-k">=</span><span class="pl-smi">l</span>.<span class="pl-en">extend</span>({cfg<span class="pl-k">:</span><span class="pl-smi">l</span>.<span class="pl-en">extend</span>({format<span class="pl-k">:</span>b}),<span class="pl-en">encrypt</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">a</span>,<span class="pl-smi">b</span>,<span class="pl-smi">c</span>,<span class="pl-smi">d</span>){d<span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-smi">cfg</span>.<span class="pl-en">extend</span>(d);<span class="pl-k">var</span> l<span class="pl-k">=</span><span class="pl-smi">a</span>.<span class="pl-en">createEncryptor</span>(c,d);b<span class="pl-k">=</span><span class="pl-smi">l</span>.<span class="pl-en">finalize</span>(b);l<span class="pl-k">=</span><span class="pl-smi">l</span>.<span class="pl-smi">cfg</span>;<span class="pl-k">return</span> <span class="pl-smi">n</span>.<span class="pl-en">create</span>({ciphertext<span class="pl-k">:</span>b,key<span class="pl-k">:</span>c,iv<span class="pl-k">:</span><span class="pl-smi">l</span>.<span class="pl-smi">iv</span>,algorithm<span class="pl-k">:</span>a,mode<span class="pl-k">:</span><span class="pl-smi">l</span>.<span class="pl-smi">mode</span>,padding<span class="pl-k">:</span><span class="pl-smi">l</span>.<span class="pl-smi">padding</span>,blockSize<span class="pl-k">:</span><span class="pl-smi">a</span>.<span class="pl-smi">blockSize</span>,formatter<span class="pl-k">:</span><span class="pl-smi">d</span>.<span class="pl-smi">format</span>})},</td>
      </tr>
      <tr>
        <td id="L30" class="blob-num js-line-number" data-line-number="30"></td>
        <td id="LC30" class="blob-code blob-code-inner js-file-line"><span class="pl-en">decrypt</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">a</span>,<span class="pl-smi">b</span>,<span class="pl-smi">c</span>,<span class="pl-smi">d</span>){d<span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-smi">cfg</span>.<span class="pl-en">extend</span>(d);b<span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-en">_parse</span>(b,<span class="pl-smi">d</span>.<span class="pl-smi">format</span>);<span class="pl-k">return</span> <span class="pl-smi">a</span>.<span class="pl-en">createDecryptor</span>(c,d).<span class="pl-en">finalize</span>(<span class="pl-smi">b</span>.<span class="pl-smi">ciphertext</span>)},<span class="pl-en">_parse</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">a</span>,<span class="pl-smi">b</span>){<span class="pl-k">return</span><span class="pl-s"><span class="pl-pds">&quot;</span>string<span class="pl-pds">&quot;</span></span><span class="pl-k">==</span><span class="pl-k">typeof</span> a<span class="pl-k">?</span><span class="pl-smi">b</span>.<span class="pl-c1">parse</span>(a,<span class="pl-c1">this</span>)<span class="pl-k">:</span>a}}),p<span class="pl-k">=</span>(<span class="pl-smi">p</span>.<span class="pl-smi">kdf</span><span class="pl-k">=</span>{}).<span class="pl-smi">OpenSSL</span><span class="pl-k">=</span>{<span class="pl-en">execute</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">a</span>,<span class="pl-smi">b</span>,<span class="pl-smi">c</span>,<span class="pl-smi">d</span>){d<span class="pl-k">||</span>(d<span class="pl-k">=</span><span class="pl-smi">s</span>.<span class="pl-en">random</span>(<span class="pl-c1">8</span>));a<span class="pl-k">=</span><span class="pl-smi">w</span>.<span class="pl-en">create</span>({keySize<span class="pl-k">:</span>b<span class="pl-k">+</span>c}).<span class="pl-en">compute</span>(a,d);c<span class="pl-k">=</span><span class="pl-smi">s</span>.<span class="pl-en">create</span>(<span class="pl-smi">a</span>.<span class="pl-smi">words</span>.<span class="pl-c1">slice</span>(b),<span class="pl-c1">4</span><span class="pl-k">*</span>c);<span class="pl-smi">a</span>.<span class="pl-smi">sigBytes</span><span class="pl-k">=</span><span class="pl-c1">4</span><span class="pl-k">*</span>b;<span class="pl-k">return</span> <span class="pl-smi">n</span>.<span class="pl-en">create</span>({key<span class="pl-k">:</span>a,iv<span class="pl-k">:</span>c,salt<span class="pl-k">:</span>d})}},c<span class="pl-k">=</span><span class="pl-smi">d</span>.<span class="pl-smi">PasswordBasedCipher</span><span class="pl-k">=</span><span class="pl-smi">a</span>.<span class="pl-en">extend</span>({cfg<span class="pl-k">:</span><span class="pl-smi">a</span>.<span class="pl-smi">cfg</span>.<span class="pl-en">extend</span>({kdf<span class="pl-k">:</span>p}),<span class="pl-en">encrypt</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">b</span>,<span class="pl-smi">c</span>,<span class="pl-smi">d</span>,<span class="pl-smi">l</span>){l<span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-smi">cfg</span>.<span class="pl-en">extend</span>(l);d<span class="pl-k">=</span><span class="pl-smi">l</span>.<span class="pl-smi">kdf</span>.<span class="pl-en">execute</span>(d,</td>
      </tr>
      <tr>
        <td id="L31" class="blob-num js-line-number" data-line-number="31"></td>
        <td id="LC31" class="blob-code blob-code-inner js-file-line"><span class="pl-smi">b</span>.<span class="pl-smi">keySize</span>,<span class="pl-smi">b</span>.<span class="pl-smi">ivSize</span>);<span class="pl-smi">l</span>.<span class="pl-smi">iv</span><span class="pl-k">=</span><span class="pl-smi">d</span>.<span class="pl-smi">iv</span>;b<span class="pl-k">=</span><span class="pl-smi">a</span>.<span class="pl-smi">encrypt</span>.<span class="pl-c1">call</span>(<span class="pl-c1">this</span>,b,c,<span class="pl-smi">d</span>.<span class="pl-smi">key</span>,l);<span class="pl-smi">b</span>.<span class="pl-en">mixIn</span>(d);<span class="pl-k">return</span> b},<span class="pl-en">decrypt</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">b</span>,<span class="pl-smi">c</span>,<span class="pl-smi">d</span>,<span class="pl-smi">l</span>){l<span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-smi">cfg</span>.<span class="pl-en">extend</span>(l);c<span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-en">_parse</span>(c,<span class="pl-smi">l</span>.<span class="pl-smi">format</span>);d<span class="pl-k">=</span><span class="pl-smi">l</span>.<span class="pl-smi">kdf</span>.<span class="pl-en">execute</span>(d,<span class="pl-smi">b</span>.<span class="pl-smi">keySize</span>,<span class="pl-smi">b</span>.<span class="pl-smi">ivSize</span>,<span class="pl-smi">c</span>.<span class="pl-smi">salt</span>);<span class="pl-smi">l</span>.<span class="pl-smi">iv</span><span class="pl-k">=</span><span class="pl-smi">d</span>.<span class="pl-smi">iv</span>;<span class="pl-k">return</span> <span class="pl-smi">a</span>.<span class="pl-smi">decrypt</span>.<span class="pl-c1">call</span>(<span class="pl-c1">this</span>,b,c,<span class="pl-smi">d</span>.<span class="pl-smi">key</span>,l)}})}();</td>
      </tr>
      <tr>
        <td id="L32" class="blob-num js-line-number" data-line-number="32"></td>
        <td id="LC32" class="blob-code blob-code-inner js-file-line">(<span class="pl-k">function</span>(){<span class="pl-k">for</span>(<span class="pl-k">var</span> u<span class="pl-k">=</span>CryptoJS,p<span class="pl-k">=</span><span class="pl-smi">u</span>.<span class="pl-smi">lib</span>.<span class="pl-smi">BlockCipher</span>,d<span class="pl-k">=</span><span class="pl-smi">u</span>.<span class="pl-smi">algo</span>,l<span class="pl-k">=</span>[],s<span class="pl-k">=</span>[],t<span class="pl-k">=</span>[],r<span class="pl-k">=</span>[],w<span class="pl-k">=</span>[],v<span class="pl-k">=</span>[],b<span class="pl-k">=</span>[],x<span class="pl-k">=</span>[],q<span class="pl-k">=</span>[],n<span class="pl-k">=</span>[],a<span class="pl-k">=</span>[],c<span class="pl-k">=</span><span class="pl-c1">0</span>;<span class="pl-c1">256</span><span class="pl-k">&gt;</span>c;c<span class="pl-k">++</span>)a[c]<span class="pl-k">=</span><span class="pl-c1">128</span><span class="pl-k">&gt;</span>c<span class="pl-k">?</span>c<span class="pl-k">&lt;&lt;</span><span class="pl-c1">1</span><span class="pl-k">:</span>c<span class="pl-k">&lt;&lt;</span><span class="pl-c1">1</span><span class="pl-k">^</span><span class="pl-c1">283</span>;<span class="pl-k">for</span>(<span class="pl-k">var</span> e<span class="pl-k">=</span><span class="pl-c1">0</span>,j<span class="pl-k">=</span><span class="pl-c1">0</span>,c<span class="pl-k">=</span><span class="pl-c1">0</span>;<span class="pl-c1">256</span><span class="pl-k">&gt;</span>c;c<span class="pl-k">++</span>){<span class="pl-k">var</span> k<span class="pl-k">=</span>j<span class="pl-k">^</span>j<span class="pl-k">&lt;&lt;</span><span class="pl-c1">1</span><span class="pl-k">^</span>j<span class="pl-k">&lt;&lt;</span><span class="pl-c1">2</span><span class="pl-k">^</span>j<span class="pl-k">&lt;&lt;</span><span class="pl-c1">3</span><span class="pl-k">^</span>j<span class="pl-k">&lt;&lt;</span><span class="pl-c1">4</span>,k<span class="pl-k">=</span>k<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">8</span><span class="pl-k">^</span>k<span class="pl-k">&amp;</span><span class="pl-c1">255</span><span class="pl-k">^</span><span class="pl-c1">99</span>;l[e]<span class="pl-k">=</span>k;s[k]<span class="pl-k">=</span>e;<span class="pl-k">var</span> z<span class="pl-k">=</span>a[e],<span class="pl-c1">F</span><span class="pl-k">=</span>a[z],<span class="pl-c1">G</span><span class="pl-k">=</span>a[<span class="pl-c1">F</span>],y<span class="pl-k">=</span><span class="pl-c1">257</span><span class="pl-k">*</span>a[k]<span class="pl-k">^</span><span class="pl-c1">16843008</span><span class="pl-k">*</span>k;t[e]<span class="pl-k">=</span>y<span class="pl-k">&lt;&lt;</span><span class="pl-c1">24</span><span class="pl-k">|</span>y<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">8</span>;r[e]<span class="pl-k">=</span>y<span class="pl-k">&lt;&lt;</span><span class="pl-c1">16</span><span class="pl-k">|</span>y<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">16</span>;w[e]<span class="pl-k">=</span>y<span class="pl-k">&lt;&lt;</span><span class="pl-c1">8</span><span class="pl-k">|</span>y<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">24</span>;v[e]<span class="pl-k">=</span>y;y<span class="pl-k">=</span><span class="pl-c1">16843009</span><span class="pl-k">*</span><span class="pl-c1">G</span><span class="pl-k">^</span><span class="pl-c1">65537</span><span class="pl-k">*</span><span class="pl-c1">F</span><span class="pl-k">^</span><span class="pl-c1">257</span><span class="pl-k">*</span>z<span class="pl-k">^</span><span class="pl-c1">16843008</span><span class="pl-k">*</span>e;b[k]<span class="pl-k">=</span>y<span class="pl-k">&lt;&lt;</span><span class="pl-c1">24</span><span class="pl-k">|</span>y<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">8</span>;x[k]<span class="pl-k">=</span>y<span class="pl-k">&lt;&lt;</span><span class="pl-c1">16</span><span class="pl-k">|</span>y<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">16</span>;q[k]<span class="pl-k">=</span>y<span class="pl-k">&lt;&lt;</span><span class="pl-c1">8</span><span class="pl-k">|</span>y<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">24</span>;n[k]<span class="pl-k">=</span>y;e<span class="pl-k">?</span>(e<span class="pl-k">=</span>z<span class="pl-k">^</span>a[a[a[<span class="pl-c1">G</span><span class="pl-k">^</span>z]]],j<span class="pl-k">^=</span>a[a[j]])<span class="pl-k">:</span>e<span class="pl-k">=</span>j<span class="pl-k">=</span><span class="pl-c1">1</span>}<span class="pl-k">var</span> <span class="pl-c1">H</span><span class="pl-k">=</span>[<span class="pl-c1">0</span>,<span class="pl-c1">1</span>,<span class="pl-c1">2</span>,<span class="pl-c1">4</span>,<span class="pl-c1">8</span>,</td>
      </tr>
      <tr>
        <td id="L33" class="blob-num js-line-number" data-line-number="33"></td>
        <td id="LC33" class="blob-code blob-code-inner js-file-line"><span class="pl-c1">16</span>,<span class="pl-c1">32</span>,<span class="pl-c1">64</span>,<span class="pl-c1">128</span>,<span class="pl-c1">27</span>,<span class="pl-c1">54</span>],d<span class="pl-k">=</span><span class="pl-smi">d</span>.<span class="pl-c1">AES</span><span class="pl-k">=</span><span class="pl-smi">p</span>.<span class="pl-en">extend</span>({<span class="pl-en">_doReset</span><span class="pl-k">:</span><span class="pl-k">function</span>(){<span class="pl-k">for</span>(<span class="pl-k">var</span> a<span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-smi">_key</span>,c<span class="pl-k">=</span><span class="pl-smi">a</span>.<span class="pl-smi">words</span>,d<span class="pl-k">=</span><span class="pl-smi">a</span>.<span class="pl-smi">sigBytes</span><span class="pl-k">/</span><span class="pl-c1">4</span>,a<span class="pl-k">=</span><span class="pl-c1">4</span><span class="pl-k">*</span>((<span class="pl-c1">this</span>.<span class="pl-smi">_nRounds</span><span class="pl-k">=</span>d<span class="pl-k">+</span><span class="pl-c1">6</span>)<span class="pl-k">+</span><span class="pl-c1">1</span>),e<span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-smi">_keySchedule</span><span class="pl-k">=</span>[],j<span class="pl-k">=</span><span class="pl-c1">0</span>;j<span class="pl-k">&lt;</span>a;j<span class="pl-k">++</span>)<span class="pl-k">if</span>(j<span class="pl-k">&lt;</span>d)e[j]<span class="pl-k">=</span>c[j];<span class="pl-k">else</span>{<span class="pl-k">var</span> k<span class="pl-k">=</span>e[j<span class="pl-k">-</span><span class="pl-c1">1</span>];j<span class="pl-k">%</span>d<span class="pl-k">?</span><span class="pl-c1">6</span><span class="pl-k">&lt;</span>d<span class="pl-k">&amp;&amp;</span><span class="pl-c1">4</span><span class="pl-k">==</span>j<span class="pl-k">%</span>d<span class="pl-k">&amp;&amp;</span>(k<span class="pl-k">=</span>l[k<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">24</span>]<span class="pl-k">&lt;&lt;</span><span class="pl-c1">24</span><span class="pl-k">|</span>l[k<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">16</span><span class="pl-k">&amp;</span><span class="pl-c1">255</span>]<span class="pl-k">&lt;&lt;</span><span class="pl-c1">16</span><span class="pl-k">|</span>l[k<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">8</span><span class="pl-k">&amp;</span><span class="pl-c1">255</span>]<span class="pl-k">&lt;&lt;</span><span class="pl-c1">8</span><span class="pl-k">|</span>l[k<span class="pl-k">&amp;</span><span class="pl-c1">255</span>])<span class="pl-k">:</span>(k<span class="pl-k">=</span>k<span class="pl-k">&lt;&lt;</span><span class="pl-c1">8</span><span class="pl-k">|</span>k<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">24</span>,k<span class="pl-k">=</span>l[k<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">24</span>]<span class="pl-k">&lt;&lt;</span><span class="pl-c1">24</span><span class="pl-k">|</span>l[k<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">16</span><span class="pl-k">&amp;</span><span class="pl-c1">255</span>]<span class="pl-k">&lt;&lt;</span><span class="pl-c1">16</span><span class="pl-k">|</span>l[k<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">8</span><span class="pl-k">&amp;</span><span class="pl-c1">255</span>]<span class="pl-k">&lt;&lt;</span><span class="pl-c1">8</span><span class="pl-k">|</span>l[k<span class="pl-k">&amp;</span><span class="pl-c1">255</span>],k<span class="pl-k">^=</span><span class="pl-c1">H</span>[j<span class="pl-k">/</span>d<span class="pl-k">|</span><span class="pl-c1">0</span>]<span class="pl-k">&lt;&lt;</span><span class="pl-c1">24</span>);e[j]<span class="pl-k">=</span>e[j<span class="pl-k">-</span>d]<span class="pl-k">^</span>k}c<span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-smi">_invKeySchedule</span><span class="pl-k">=</span>[];<span class="pl-k">for</span>(d<span class="pl-k">=</span><span class="pl-c1">0</span>;d<span class="pl-k">&lt;</span>a;d<span class="pl-k">++</span>)j<span class="pl-k">=</span>a<span class="pl-k">-</span>d,k<span class="pl-k">=</span>d<span class="pl-k">%</span><span class="pl-c1">4</span><span class="pl-k">?</span>e[j]<span class="pl-k">:</span>e[j<span class="pl-k">-</span><span class="pl-c1">4</span>],c[d]<span class="pl-k">=</span><span class="pl-c1">4</span><span class="pl-k">&gt;</span>d<span class="pl-k">||</span><span class="pl-c1">4</span><span class="pl-k">&gt;=</span>j<span class="pl-k">?</span>k<span class="pl-k">:</span>b[l[k<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">24</span>]]<span class="pl-k">^</span>x[l[k<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">16</span><span class="pl-k">&amp;</span><span class="pl-c1">255</span>]]<span class="pl-k">^</span>q[l[k<span class="pl-k">&gt;&gt;&gt;</span></td>
      </tr>
      <tr>
        <td id="L34" class="blob-num js-line-number" data-line-number="34"></td>
        <td id="LC34" class="blob-code blob-code-inner js-file-line"><span class="pl-c1">8</span><span class="pl-k">&amp;</span><span class="pl-c1">255</span>]]<span class="pl-k">^</span>n[l[k<span class="pl-k">&amp;</span><span class="pl-c1">255</span>]]},<span class="pl-en">encryptBlock</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">a</span>,<span class="pl-smi">b</span>){<span class="pl-c1">this</span>.<span class="pl-en">_doCryptBlock</span>(a,b,<span class="pl-c1">this</span>.<span class="pl-smi">_keySchedule</span>,t,r,w,v,l)},<span class="pl-en">decryptBlock</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">a</span>,<span class="pl-smi">c</span>){<span class="pl-k">var</span> d<span class="pl-k">=</span>a[c<span class="pl-k">+</span><span class="pl-c1">1</span>];a[c<span class="pl-k">+</span><span class="pl-c1">1</span>]<span class="pl-k">=</span>a[c<span class="pl-k">+</span><span class="pl-c1">3</span>];a[c<span class="pl-k">+</span><span class="pl-c1">3</span>]<span class="pl-k">=</span>d;<span class="pl-c1">this</span>.<span class="pl-en">_doCryptBlock</span>(a,c,<span class="pl-c1">this</span>.<span class="pl-smi">_invKeySchedule</span>,b,x,q,n,s);d<span class="pl-k">=</span>a[c<span class="pl-k">+</span><span class="pl-c1">1</span>];a[c<span class="pl-k">+</span><span class="pl-c1">1</span>]<span class="pl-k">=</span>a[c<span class="pl-k">+</span><span class="pl-c1">3</span>];a[c<span class="pl-k">+</span><span class="pl-c1">3</span>]<span class="pl-k">=</span>d},<span class="pl-en">_doCryptBlock</span><span class="pl-k">:</span><span class="pl-k">function</span>(<span class="pl-smi">a</span>,<span class="pl-smi">b</span>,<span class="pl-smi">c</span>,<span class="pl-smi">d</span>,<span class="pl-smi">e</span>,<span class="pl-smi">j</span>,<span class="pl-smi">l</span>,<span class="pl-smi">f</span>){<span class="pl-k">for</span>(<span class="pl-k">var</span> m<span class="pl-k">=</span><span class="pl-c1">this</span>.<span class="pl-smi">_nRounds</span>,g<span class="pl-k">=</span>a[b]<span class="pl-k">^</span>c[<span class="pl-c1">0</span>],h<span class="pl-k">=</span>a[b<span class="pl-k">+</span><span class="pl-c1">1</span>]<span class="pl-k">^</span>c[<span class="pl-c1">1</span>],k<span class="pl-k">=</span>a[b<span class="pl-k">+</span><span class="pl-c1">2</span>]<span class="pl-k">^</span>c[<span class="pl-c1">2</span>],n<span class="pl-k">=</span>a[b<span class="pl-k">+</span><span class="pl-c1">3</span>]<span class="pl-k">^</span>c[<span class="pl-c1">3</span>],p<span class="pl-k">=</span><span class="pl-c1">4</span>,r<span class="pl-k">=</span><span class="pl-c1">1</span>;r<span class="pl-k">&lt;</span>m;r<span class="pl-k">++</span>)<span class="pl-k">var</span> q<span class="pl-k">=</span>d[g<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">24</span>]<span class="pl-k">^</span>e[h<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">16</span><span class="pl-k">&amp;</span><span class="pl-c1">255</span>]<span class="pl-k">^</span>j[k<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">8</span><span class="pl-k">&amp;</span><span class="pl-c1">255</span>]<span class="pl-k">^</span>l[n<span class="pl-k">&amp;</span><span class="pl-c1">255</span>]<span class="pl-k">^</span>c[p<span class="pl-k">++</span>],s<span class="pl-k">=</span>d[h<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">24</span>]<span class="pl-k">^</span>e[k<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">16</span><span class="pl-k">&amp;</span><span class="pl-c1">255</span>]<span class="pl-k">^</span>j[n<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">8</span><span class="pl-k">&amp;</span><span class="pl-c1">255</span>]<span class="pl-k">^</span>l[g<span class="pl-k">&amp;</span><span class="pl-c1">255</span>]<span class="pl-k">^</span>c[p<span class="pl-k">++</span>],t<span class="pl-k">=</span></td>
      </tr>
      <tr>
        <td id="L35" class="blob-num js-line-number" data-line-number="35"></td>
        <td id="LC35" class="blob-code blob-code-inner js-file-line">d[k<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">24</span>]<span class="pl-k">^</span>e[n<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">16</span><span class="pl-k">&amp;</span><span class="pl-c1">255</span>]<span class="pl-k">^</span>j[g<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">8</span><span class="pl-k">&amp;</span><span class="pl-c1">255</span>]<span class="pl-k">^</span>l[h<span class="pl-k">&amp;</span><span class="pl-c1">255</span>]<span class="pl-k">^</span>c[p<span class="pl-k">++</span>],n<span class="pl-k">=</span>d[n<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">24</span>]<span class="pl-k">^</span>e[g<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">16</span><span class="pl-k">&amp;</span><span class="pl-c1">255</span>]<span class="pl-k">^</span>j[h<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">8</span><span class="pl-k">&amp;</span><span class="pl-c1">255</span>]<span class="pl-k">^</span>l[k<span class="pl-k">&amp;</span><span class="pl-c1">255</span>]<span class="pl-k">^</span>c[p<span class="pl-k">++</span>],g<span class="pl-k">=</span>q,h<span class="pl-k">=</span>s,k<span class="pl-k">=</span>t;q<span class="pl-k">=</span>(f[g<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">24</span>]<span class="pl-k">&lt;&lt;</span><span class="pl-c1">24</span><span class="pl-k">|</span>f[h<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">16</span><span class="pl-k">&amp;</span><span class="pl-c1">255</span>]<span class="pl-k">&lt;&lt;</span><span class="pl-c1">16</span><span class="pl-k">|</span>f[k<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">8</span><span class="pl-k">&amp;</span><span class="pl-c1">255</span>]<span class="pl-k">&lt;&lt;</span><span class="pl-c1">8</span><span class="pl-k">|</span>f[n<span class="pl-k">&amp;</span><span class="pl-c1">255</span>])<span class="pl-k">^</span>c[p<span class="pl-k">++</span>];s<span class="pl-k">=</span>(f[h<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">24</span>]<span class="pl-k">&lt;&lt;</span><span class="pl-c1">24</span><span class="pl-k">|</span>f[k<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">16</span><span class="pl-k">&amp;</span><span class="pl-c1">255</span>]<span class="pl-k">&lt;&lt;</span><span class="pl-c1">16</span><span class="pl-k">|</span>f[n<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">8</span><span class="pl-k">&amp;</span><span class="pl-c1">255</span>]<span class="pl-k">&lt;&lt;</span><span class="pl-c1">8</span><span class="pl-k">|</span>f[g<span class="pl-k">&amp;</span><span class="pl-c1">255</span>])<span class="pl-k">^</span>c[p<span class="pl-k">++</span>];t<span class="pl-k">=</span>(f[k<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">24</span>]<span class="pl-k">&lt;&lt;</span><span class="pl-c1">24</span><span class="pl-k">|</span>f[n<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">16</span><span class="pl-k">&amp;</span><span class="pl-c1">255</span>]<span class="pl-k">&lt;&lt;</span><span class="pl-c1">16</span><span class="pl-k">|</span>f[g<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">8</span><span class="pl-k">&amp;</span><span class="pl-c1">255</span>]<span class="pl-k">&lt;&lt;</span><span class="pl-c1">8</span><span class="pl-k">|</span>f[h<span class="pl-k">&amp;</span><span class="pl-c1">255</span>])<span class="pl-k">^</span>c[p<span class="pl-k">++</span>];n<span class="pl-k">=</span>(f[n<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">24</span>]<span class="pl-k">&lt;&lt;</span><span class="pl-c1">24</span><span class="pl-k">|</span>f[g<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">16</span><span class="pl-k">&amp;</span><span class="pl-c1">255</span>]<span class="pl-k">&lt;&lt;</span><span class="pl-c1">16</span><span class="pl-k">|</span>f[h<span class="pl-k">&gt;&gt;&gt;</span><span class="pl-c1">8</span><span class="pl-k">&amp;</span><span class="pl-c1">255</span>]<span class="pl-k">&lt;&lt;</span><span class="pl-c1">8</span><span class="pl-k">|</span>f[k<span class="pl-k">&amp;</span><span class="pl-c1">255</span>])<span class="pl-k">^</span>c[p<span class="pl-k">++</span>];a[b]<span class="pl-k">=</span>q;a[b<span class="pl-k">+</span><span class="pl-c1">1</span>]<span class="pl-k">=</span>s;a[b<span class="pl-k">+</span><span class="pl-c1">2</span>]<span class="pl-k">=</span>t;a[b<span class="pl-k">+</span><span class="pl-c1">3</span>]<span class="pl-k">=</span>n},keySize<span class="pl-k">:</span><span class="pl-c1">8</span>});<span class="pl-smi">u</span>.<span class="pl-c1">AES</span><span class="pl-k">=</span><span class="pl-smi">p</span>.<span class="pl-en">_createHelper</span>(d)})();</td>
      </tr>
</table>

  </div>

</div>

<button type="button" data-facebox="#jump-to-line" data-facebox-class="linejump" data-hotkey="l" class="d-none">Jump to Line</button>
<div id="jump-to-line" style="display:none">
  <!-- '"` --><!-- </textarea></xmp> --></option></form><form accept-charset="UTF-8" action="" class="js-jump-to-line-form" method="get"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /></div>
    <input class="form-control linejump-input js-jump-to-line-field" type="text" placeholder="Jump to line&hellip;" aria-label="Jump to line" autofocus>
    <button type="submit" class="btn">Go</button>
</form></div>


  </div>
  <div class="modal-backdrop js-touch-events"></div>
</div>

    </div>
  </div>

  </div>

      <div class="container site-footer-container">
  <div class="site-footer" role="contentinfo">
    <ul class="site-footer-links float-right">
        <li><a href="https://github.com/contact" data-ga-click="Footer, go to contact, text:contact">Contact GitHub</a></li>
      <li><a href="https://developer.github.com" data-ga-click="Footer, go to api, text:api">API</a></li>
      <li><a href="https://training.github.com" data-ga-click="Footer, go to training, text:training">Training</a></li>
      <li><a href="https://shop.github.com" data-ga-click="Footer, go to shop, text:shop">Shop</a></li>
        <li><a href="https://github.com/blog" data-ga-click="Footer, go to blog, text:blog">Blog</a></li>
        <li><a href="https://github.com/about" data-ga-click="Footer, go to about, text:about">About</a></li>

    </ul>

    <a href="https://github.com" aria-label="Homepage" class="site-footer-mark" title="GitHub">
      <svg aria-hidden="true" class="octicon octicon-mark-github" height="24" version="1.1" viewBox="0 0 16 16" width="24"><path fill-rule="evenodd" d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.013 8.013 0 0 0 16 8c0-4.42-3.58-8-8-8z"/></svg>
</a>
    <ul class="site-footer-links">
      <li>&copy; 2017 <span title="0.07927s from github-fe-dc3412d.cp1-iad.github.net">GitHub</span>, Inc.</li>
        <li><a href="https://github.com/site/terms" data-ga-click="Footer, go to terms, text:terms">Terms</a></li>
        <li><a href="https://github.com/site/privacy" data-ga-click="Footer, go to privacy, text:privacy">Privacy</a></li>
        <li><a href="https://github.com/security" data-ga-click="Footer, go to security, text:security">Security</a></li>
        <li><a href="https://status.github.com/" data-ga-click="Footer, go to status, text:status">Status</a></li>
        <li><a href="https://help.github.com" data-ga-click="Footer, go to help, text:help">Help</a></li>
    </ul>
  </div>
</div>



  

  <div id="ajax-error-message" class="ajax-error-message flash flash-error">
    <svg aria-hidden="true" class="octicon octicon-alert" height="16" version="1.1" viewBox="0 0 16 16" width="16"><path fill-rule="evenodd" d="M8.865 1.52c-.18-.31-.51-.5-.87-.5s-.69.19-.87.5L.275 13.5c-.18.31-.18.69 0 1 .19.31.52.5.87.5h13.7c.36 0 .69-.19.86-.5.17-.31.18-.69.01-1L8.865 1.52zM8.995 13h-2v-2h2v2zm0-3h-2V6h2v4z"/></svg>
    <button type="button" class="flash-close js-flash-close js-ajax-error-dismiss" aria-label="Dismiss error">
      <svg aria-hidden="true" class="octicon octicon-x" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M7.48 8l3.75 3.75-1.48 1.48L6 9.48l-3.75 3.75-1.48-1.48L4.52 8 .77 4.25l1.48-1.48L6 6.52l3.75-3.75 1.48 1.48z"/></svg>
    </button>
    You can't perform that action at this time.
  </div>


    <script crossorigin="anonymous" integrity="sha256-ikMY/+oJoM24IUt2zykmufagztMYoxe+1BnbGSFMaQ0=" src="https://assets-cdn.github.com/assets/compat-8a4318ffea09a0cdb8214b76cf2926b9f6a0ced318a317bed419db19214c690d.js"></script>
    <script crossorigin="anonymous" integrity="sha256-bRCeda2EcbpBUIJybADDX7kpzquXUIJJKDXxHsqMB9k=" src="https://assets-cdn.github.com/assets/frameworks-6d109e75ad8471ba415082726c00c35fb929ceab975082492835f11eca8c07d9.js"></script>
    <script async="async" crossorigin="anonymous" integrity="sha256-Jafjhi94Y/KevPz5fBSZ7I8wWziDw/HZRy8EVu8Fcxo=" src="https://assets-cdn.github.com/assets/github-25a7e3862f7863f29ebcfcf97c1499ec8f305b3883c3f1d9472f0456ef05731a.js"></script>
    
    
    
    
  <div class="js-stale-session-flash stale-session-flash flash flash-warn flash-banner d-none">
    <svg aria-hidden="true" class="octicon octicon-alert" height="16" version="1.1" viewBox="0 0 16 16" width="16"><path fill-rule="evenodd" d="M8.865 1.52c-.18-.31-.51-.5-.87-.5s-.69.19-.87.5L.275 13.5c-.18.31-.18.69 0 1 .19.31.52.5.87.5h13.7c.36 0 .69-.19.86-.5.17-.31.18-.69.01-1L8.865 1.52zM8.995 13h-2v-2h2v2zm0-3h-2V6h2v4z"/></svg>
    <span class="signed-in-tab-flash">You signed in with another tab or window. <a href="">Reload</a> to refresh your session.</span>
    <span class="signed-out-tab-flash">You signed out in another tab or window. <a href="">Reload</a> to refresh your session.</span>
  </div>
  <div class="facebox" id="facebox" style="display:none;">
  <div class="facebox-popup">
    <div class="facebox-content" role="dialog" aria-labelledby="facebox-header" aria-describedby="facebox-description">
    </div>
    <button type="button" class="facebox-close js-facebox-close" aria-label="Close modal">
      <svg aria-hidden="true" class="octicon octicon-x" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M7.48 8l3.75 3.75-1.48 1.48L6 9.48l-3.75 3.75-1.48-1.48L4.52 8 .77 4.25l1.48-1.48L6 6.52l3.75-3.75 1.48 1.48z"/></svg>
    </button>
  </div>
</div>


  </body>
</html>

