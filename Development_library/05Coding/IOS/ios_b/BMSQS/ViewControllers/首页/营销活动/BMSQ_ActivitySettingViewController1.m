//
//  BMSQ_ActivitySettingViewController.m
//  BMSQS
//
//  Created by Sencho Kong on 15/8/26.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_ActivitySettingViewController1.h"
#import "BMSQ_ActivitySettingViewController3.h"


@interface BMSQ_ActivitySettingViewController1 ()<UITextViewDelegate>

@property (weak, nonatomic) IBOutlet UIButton *nextButton;
@property (weak, nonatomic) IBOutlet UIButton *preButton;
@property (weak, nonatomic) IBOutlet UITextView *textView;


@end

@implementation BMSQ_ActivitySettingViewController1

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setNavTitle:@"营销活动设置"];
    [self setNavBackItem];
    
    _nextButton.layer.cornerRadius = 3.0;
    [_textView becomeFirstResponder];
    
}



- (IBAction)didClickNextButton:(id)sender {
    
    NSString *des ;
    if (_textView.text.length == 0 || !_textView.text) {
        
        UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:nil message:@"请输入活动描述" delegate:nil cancelButtonTitle:nil otherButtonTitles:@"OK", nil];
        [alterView show];
        return;
    }else{
        des = _textView.text;
    }

    NSMutableDictionary *dataDic = [_dataDic mutableCopy];
    [dataDic setObject:des forKey:@"txtContent"];
    
    BMSQ_ActivitySettingViewController3 *pushVC = [[BMSQ_ActivitySettingViewController3 alloc] init];
    pushVC.dataDic = dataDic;
    [self.navigationController pushViewController:pushVC animated:YES];
    
  
    
}

@end
