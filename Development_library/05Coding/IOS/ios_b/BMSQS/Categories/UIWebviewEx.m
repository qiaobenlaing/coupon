//
//  UIWebviewEx.m
//  SDBooking
//
//  Created by djx on 14-1-25.
//  Copyright (c) 2014年 djx. All rights reserved.
//

#import "UIWebviewEx.h"

@implementation UIWebviewEx

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/


//通过Url加载网页，传入的nsstring格式的url
- (void)loadRequestWithString:(NSString*)requestString
{
    if (requestString == nil || requestString.length <= 0)
    {
        //传入参数为空
        return;
    }
    
    NSURL* url = [NSURL URLWithString:requestString];
    [self loadRequest:[NSURLRequest requestWithURL:url]];
}

//加载本地html
- (void)loadHTMLStringByName:(NSString *)htmlName
{
    if (htmlName == nil || htmlName.length <= 0)
    {
        return;
    }
    
    NSString *resourcePath = [ [NSBundle mainBundle] resourcePath];
    NSString *filePath  = [resourcePath stringByAppendingPathComponent:htmlName];
    
    NSError *error = nil;
    NSString *htmlstring =[[NSString alloc] initWithContentsOfFile:filePath encoding:NSUTF8StringEncoding error:&error];
    
    if (error != nil)
    {
        return;
    }
    [self loadHTMLString:htmlstring  baseURL:[NSURL fileURLWithPath: [[NSBundle mainBundle]  bundlePath]]];
    
}

//执行js的某个方法
- (void)performSelectorJS:(NSString*)functionName
{
    if (functionName == nil || functionName.length <= 0)
    {
        return;
    }
    
    [self stringByEvaluatingJavaScriptFromString:functionName];
}

- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType
{
    return YES;
}

@end
