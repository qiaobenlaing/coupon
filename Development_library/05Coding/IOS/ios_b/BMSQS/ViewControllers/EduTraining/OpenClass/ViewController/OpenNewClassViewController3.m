//
//  OpenNewClassViewController3.m
//  BMSQS
//
//  Created by gh on 16/3/10.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "OpenNewClassViewController3.h"
#import "OpenNewClassViewController4.h"

@interface OpenNewClassViewController3 ()

@property (nonatomic, strong)UITextView *textView ;

@end

@implementation OpenNewClassViewController3

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setViewUp];
    
    
}

- (void)setViewUp {
    [self setNavBackItem];
    [self setNavTitle:@"开班"];
    [self setRightBtn];

    
    self.textView = [[UITextView alloc] initWithFrame:CGRectMake(10, APP_VIEW_ORIGIN_Y+10, APP_VIEW_WIDTH-20, APP_VIEW_HEIGHT/3)];
    self.textView.layer.borderColor = APP_CELL_LINE_COLOR.CGColor;
    self.textView.layer.borderWidth = 1.0;
    self.textView.font = [UIFont systemFontOfSize:14.f];
    [self.view addSubview:self.textView];
 
}





#pragma mark - button 点击事件
- (void)btnAction:(UIButton *)button {
    
    if (button.tag == 1000) { //下一步
        NSString *textString = self.textView.text;
        
        if (textString.length == 0) {
            CSAlert(@"请填写课程简介");
            return;
        }
        
        
        
        OpenNewClassViewController4 *vc = [[OpenNewClassViewController4 alloc] init];
        vc.requestDic = [NSMutableDictionary dictionaryWithDictionary:self.requestDic];
        [vc.requestDic setObject:textString forKey:@"classInfo"]; // 课程简介
        [self.navigationController pushViewController:vc animated:YES];
        
    }
    
    
}

//  下一步
- (void)setRightBtn {
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    button.frame = CGRectMake(APP_VIEW_WIDTH-74, (44-APP_NAV_LEFT_ITEM_HEIGHT)/2 + APP_STATUSBAR_HEIGHT, 64, APP_NAV_LEFT_ITEM_HEIGHT);
    button.tag = 1000;
    button.backgroundColor = [UIColor clearColor];
    [button setTitle:@"下一步" forState:UIControlStateNormal];
    [button addTarget:self action:@selector(btnAction:) forControlEvents:UIControlEventTouchUpInside];
    [button.titleLabel setTextAlignment:NSTextAlignmentRight];
    [self setRight:button];
    
    
}
@end
