//
//  BMSQ_ShortDesViewController.m
//  BMSQS
//
//  Created by gh on 15/12/3.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_ShortDesViewController.h"

@interface BMSQ_ShortDesViewController()<UITextViewDelegate>

@end


@implementation BMSQ_ShortDesViewController 

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setViewUp];
    
}


- (void)setViewUp {
    
    [self setNavTitle:@"店铺介绍"];
    

    UITextView *shortDesText = [[UITextView alloc] initWithFrame:CGRectMake(10, APP_VIEW_ORIGIN_Y + 10, APP_VIEW_WIDTH-20, 30)];
    shortDesText.font = [UIFont systemFontOfSize:13];
    shortDesText.backgroundColor = [UIColor whiteColor];
    shortDesText.delegate = self;
    shortDesText.tag = 1001;
    if (self.textValue.length>0) {
        shortDesText.text = self.textValue;
    }
    
    CGRect frame = shortDesText.frame;
    frame.size.height = shortDesText.contentSize.height;
    shortDesText.frame = frame;
    [self.view addSubview:shortDesText];
    
}

- (void)didClickRightButton:(UIButton *)sender {
    UITextView *textView = [self.view viewWithTag:1001];
    NSLog(@"完成%@",textView.text);
    if (textView.text.length <= 0) {
        CSAlert(@"请输入简介");
        return;
    }

    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    
    NSString* vcode = [gloabFunction getSign:@"updateShop" strParams:[gloabFunction getShopCode]];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    NSString *updateKey = @"shortDes";
    NSString *updateValue = [NSString stringWithFormat:@"%@",textView.text];
    
    [params setObject:updateKey forKey:@"updateKey"];
    [params setObject:updateValue forKey:@"updateValue"];
    
    __block typeof(self) weakSelf = self;
    
    [self.jsonPrcClient invokeMethod:@"updateShop" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [ProgressManage closeProgress];
        NSNumber * code = [responseObject objectForKey:@"code"];
        
        if (code.intValue == 50000) {
            [self.navigationController popViewControllerAnimated:YES];
            
        }else {
            CSAlert(@"修改失败");
            
        }
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
        CSAlert(@"修改失败");
    }];
    

    
}

#pragma mark - UITextView delegate
- (void)textViewDidChange:(UITextView *)textView {

    CGRect frame = textView.frame;
    frame.size.height = textView.contentSize.height;
    textView.frame = frame;
    
    
}




@end
