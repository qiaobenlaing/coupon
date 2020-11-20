//
//  UIWebviewEx.h
//  SDBooking
//
//  Created by djx on 14-1-25.
//  Copyright (c) 2014年 djx. All rights reserved.
//


//webview扩展类
#import <UIKit/UIKit.h>

@interface UIWebviewEx : UIWebView

//某些特定的对象存储
@property(nonatomic,strong)id object;

//通过Url加载网页，传入的nsstring格式的url
- (void)loadRequestWithString:(NSString*)requestString;

//加载本地html
- (void)loadHTMLStringByName:(NSString *)htmlName;
//执行js的某个方法
- (void)performSelectorJS:(NSString*)functionName;


@end
