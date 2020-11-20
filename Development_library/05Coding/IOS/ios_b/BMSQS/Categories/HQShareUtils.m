//
//  HQShareUtils.m
//  BMSQS
//
//  Created by gh on 15/11/3.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "HQShareUtils.h"

#import <ShareSDK/ShareSDK.h>

@implementation HQShareUtils




//优惠券分享
+ (void)shareCouponWithTitle:(NSString *)title content:(NSString *)remark url:(NSString *)url{
    
    NSString *imagePath = [[NSBundle mainBundle] pathForResource:@"share" ofType:@"png"];
    id<ISSContent> publishContent = [ShareSDK content:remark
                                       defaultContent:nil
                                                image:[ShareSDK imageWithPath:imagePath]
                                                title:title
                                                  url:url
                                          description:remark
                                            mediaType:SSPublishContentMediaTypeNews];
    [self share:publishContent];
    
}



+ (void)share:(id<ISSContent>)publishContent {
    id<ISSAuthOptions> authOptions = [ShareSDK authOptionsWithAutoAuth:YES
                                                         allowCallback:YES
                                                         authViewStyle:SSAuthViewStyleFullScreenPopup
                                                          viewDelegate:nil
                                               authManagerViewDelegate:appDelegate.viewDelegate];
    
    //在授权页面中添加关注官方微博
    [authOptions setFollowAccounts:[NSDictionary dictionaryWithObjectsAndKeys:
                                    [ShareSDK userFieldWithType:SSUserFieldTypeName value:@"ShareSDK"],
                                    SHARE_TYPE_NUMBER(ShareTypeSinaWeibo),
                                    [ShareSDK userFieldWithType:SSUserFieldTypeName value:@"ShareSDK"],
                                    SHARE_TYPE_NUMBER(ShareTypeTencentWeibo),
                                    nil]];
    
    
    id<ISSShareActionSheetItem> QQSpaceItem = [ShareSDK shareActionSheetItemWithTitle:[ShareSDK getClientNameWithType:ShareTypeQQSpace]
                                                                                 icon:[ShareSDK getClientIconWithType:ShareTypeQQSpace]
                                                                         clickHandler:^{
                                                                             [ShareSDK shareContent:publishContent
                                                                                               type:ShareTypeQQSpace
                                                                                        authOptions:authOptions
                                                                                      statusBarTips:YES
                                                                                             result:^(ShareType type, SSResponseState state, id<ISSPlatformShareInfo> statusInfo, id<ICMErrorInfo> error, BOOL end) {
                                                                                                 
                                                                                                 if (state == SSPublishContentStateSuccess)
                                                                                                 {
                                                                                                     [self showAlertView:@"分享成功"];
                                                                                                     
                                                                                                 }
                                                                                                 else if (state == SSPublishContentStateFail)
                                                                                                 {
                                                                                                     [self showAlertView:@"分享失败"];
                                                                                                     
                                                                                                 }
                                                                                             }];
                                                                         }];
    
    id<ISSShareActionSheetItem> wxSessionItem = [ShareSDK shareActionSheetItemWithTitle:[ShareSDK getClientNameWithType:ShareTypeWeixiSession]
                                                                                   icon:[ShareSDK getClientIconWithType:ShareTypeWeixiSession]
                                                                           clickHandler:^{
                                                                               [ShareSDK shareContent:publishContent
                                                                                                 type:ShareTypeWeixiSession
                                                                                          authOptions:authOptions
                                                                                        statusBarTips:YES
                                                                                               result:^(ShareType type, SSResponseState state, id<ISSPlatformShareInfo> statusInfo, id<ICMErrorInfo> error, BOOL end) {
                                                                                                   
                                                                                                   if (state == SSPublishContentStateSuccess)
                                                                                                   {
                                                                                                       [self showAlertView:@"分享成功"];
                                                                                                       
                                                                                                   }
                                                                                                   else if (state == SSPublishContentStateFail)
                                                                                                   {
                                                                                                       [self showAlertView:@"分享失败"];
                                                                                                       
                                                                                                   }
                                                                                               }];
                                                                           }];
    
    id<ISSShareActionSheetItem> wxTimeLineItem = [ShareSDK shareActionSheetItemWithTitle:[ShareSDK getClientNameWithType:ShareTypeWeixiTimeline]
                                                                                    icon:[ShareSDK getClientIconWithType:ShareTypeWeixiTimeline]
                                                                            clickHandler:^{
                                                                                [ShareSDK shareContent:publishContent
                                                                                                  type:ShareTypeWeixiTimeline
                                                                                           authOptions:authOptions
                                                                                         statusBarTips:YES
                                                                                                result:^(ShareType type, SSResponseState state, id<ISSPlatformShareInfo> statusInfo, id<ICMErrorInfo> error, BOOL end) {
                                                                                                    
                                                                                                    if (state == SSPublishContentStateSuccess)
                                                                                                    {
                                                                                                        [self showAlertView:@"分享成功"];
                                                                                                        
                                                                                                    }
                                                                                                    else if (state == SSPublishContentStateFail)
                                                                                                    {
                                                                                                        [self showAlertView:@"分享失败"];
                                                                                                        
                                                                                                    }
                                                                                                }];
                                                                            }];
    
    id<ISSShareActionSheetItem> QQItem = [ShareSDK shareActionSheetItemWithTitle:[ShareSDK getClientNameWithType:ShareTypeQQ]
                                                                            icon:[ShareSDK getClientIconWithType:ShareTypeQQ]
                                                                    clickHandler:^{
                                                                        [ShareSDK shareContent:publishContent
                                                                                          type:ShareTypeQQ
                                                                                   authOptions:authOptions
                                                                                 statusBarTips:YES
                                                                                        result:^(ShareType type, SSResponseState state, id<ISSPlatformShareInfo> statusInfo, id<ICMErrorInfo> error, BOOL end) {
                                                                                            
                                                                                            if (state == SSPublishContentStateSuccess)
                                                                                            {
                                                                                                [self showAlertView:@"分享成功"];
                                                                                                
                                                                                            }
                                                                                            else if (state == SSPublishContentStateFail)
                                                                                            {
                                                                                                [self showAlertView:@"分享失败"];
                                                                                                
                                                                                            }
                                                                                        }];
                                                                    }];
    
    
    
    NSArray *shareList = [ShareSDK customShareListWithType:QQSpaceItem,wxSessionItem,wxTimeLineItem,QQItem,nil];
    
    
    //创建容器
    id<ISSContainer> container = [ShareSDK container];
    
    
    
    //显示分享菜单
    [ShareSDK showShareActionSheet:container
                         shareList:shareList
                           content:publishContent
                     statusBarTips:YES
                       authOptions:authOptions
                      shareOptions:[ShareSDK defaultShareOptionsWithTitle:nil
                                                          oneKeyShareList:[NSArray defaultOneKeyShareList]
                                                           qqButtonHidden:NO
                                                    wxSessionButtonHidden:NO
                                                   wxTimelineButtonHidden:NO
                                                     showKeyboardOnAppear:NO
                                                        shareViewDelegate:appDelegate.viewDelegate
                                                      friendsViewDelegate:appDelegate.viewDelegate
                                                    picViewerViewDelegate:nil]
                            result:^(ShareType type, SSResponseState state, id<ISSPlatformShareInfo> statusInfo, id<ICMErrorInfo> error, BOOL end) {
                                
                                if (state == SSPublishContentStateSuccess)
                                {
                                    [self showAlertView:@"分享成功"];
                                    
                                }
                                else if (state == SSPublishContentStateFail)
                                {
                                    [self showAlertView:@"分享失败"];
                                    
                                }
                            }];
    

}

+ (void)showAlertView:(NSString *)msg
{
    UIAlertView* alert = [[UIAlertView alloc]initWithTitle:nil message:msg delegate:nil cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
    [alert show];
}



@end
