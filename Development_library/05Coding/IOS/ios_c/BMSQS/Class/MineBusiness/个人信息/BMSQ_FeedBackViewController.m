//
//  SDZX_FeedBackViewController.m
//  SanSheng
//
//  Created by Sencho Kong on 13-8-13.
//  Copyright (c) 2013年 Sencho Kong. All rights reserved.
//

#import "BMSQ_FeedBackViewController.h"

@interface BMSQ_FeedBackViewController ()
- (IBAction)didClickSendButton:(id)sender;

@end

@implementation BMSQ_FeedBackViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self setNavigationBar];
    [self setNavTitle:@"对惠圈的建议"];
    [self setNavBackItem];

//    UIButton *sendButton = [UIButton buttonWithType:UIButtonTypeCustom ];
//    [sendButton setFrame:CGRectMake(-10, 0, 44, 44) ];
//    [sendButton setTitle:@"发送" forState:UIControlStateNormal];
//    [sendButton.titleLabel setFont:[UIFont boldSystemFontOfSize:17]];
//    [sendButton addTarget:self action:@selector(sendButtonClick:) forControlEvents:UIControlEventTouchUpInside];
//    
//    UIBarButtonItem *rightButtonItem = [[UIBarButtonItem alloc] initWithCustomView:sendButton];
//    UIBarButtonItem *negativeSpacer = [[UIBarButtonItem alloc]
//                                       initWithBarButtonSystemItem:UIBarButtonSystemItemFixedSpace
//                                       target:nil action:nil];
//    negativeSpacer.width = -5;
//    
//    self.navigationItem.rightBarButtonItems = [NSArray
//                                              arrayWithObjects:negativeSpacer, rightButtonItem, nil];

    self.textView  = [[UITextView alloc]initWithFrame:CGRectMake(10, APP_VIEW_ORIGIN_Y + 10, APP_VIEW_WIDTH - 20, 200)];
    self.textView.delegate = self;
    [self.view addSubview:self.textView];
    self.uilabel = [[UILabel alloc]initWithFrame:CGRectMake(5, 5, APP_VIEW_WIDTH - 30, 25)];
    
    self.uilabel.text = @"请具体描述你的意见和建议";
    [self.uilabel setTextColor:[UIColor colorWithRed:102.0/255.0 green:102.0/255.0 blue:102.0/255.0 alpha:1.0]];
    self.uilabel.enabled = NO;//lable必须设置为不可用
    self.uilabel.backgroundColor = [UIColor clearColor];
    [self.textView addSubview:self.uilabel];
    self.textView.delegate = self;
    self.uilabel.font = self.textView.font;
    
    UILabel* lb_msg = [[UILabel alloc]initWithFrame:CGRectMake(10, APP_VIEW_ORIGIN_Y + 215, APP_VIEW_WIDTH-30, 30)];
    lb_msg.text = @"感谢您对惠圈的支持,你们的建议是我们的动力";
    lb_msg.textColor = UICOLOR(140, 140, 140, 1.0);
    lb_msg.font = [UIFont systemFontOfSize:12];
    [self.view addSubview:lb_msg];
    
    UIButton* btnRightBar = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH - 44, 20, 44, 44)];
    [btnRightBar addTarget:self action:@selector(sendButtonClick) forControlEvents:UIControlEventTouchUpInside];
    [btnRightBar setTitle:@"提交" forState:UIControlStateNormal];
    [self setNavRightBarItem:btnRightBar];
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:YES];
    [self.view bringSubviewToFront:_uilabel];
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

-(void)textViewDidChange:(UITextView *)_textview
{

    if (_textView.text.length == 0)
    {
        self.uilabel.text = @"请具体描述你的意见和建议";
    }else{
        self.uilabel.text = @"";
    }
}

-(void)sendButtonClick
{
    
    if (_textView.text.length==0) {
        
        UIAlertView* alert=[[UIAlertView alloc]initWithTitle:nil message:@"请输入反馈意见" delegate:self cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
        [alert show];
        return;
    }
    
    [self updateUserInfo];

}

- (void)updateUserInfo
{
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"creatorCode"];
    [params setObject:self.textView.text forKey:@"content"];
    [params setObject:@"" forKey:@"targetCode"];
    NSString* vcode = [gloabFunction getSign:@"addFeedback" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [ProgressManage openProgressText:nil];
    [self.jsonPrcClient invokeMethod:@"addFeedback" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        UIAlertView* alert=[[UIAlertView alloc]initWithTitle:nil message:@"提交成功" delegate:self cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
        [alert show];
        
        [ProgressManage closeProgress];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
    }];
}


- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    
    [self.navigationController popViewControllerAnimated:YES];
    
}


#pragma UITextView Delegate Method

- (BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text {
    
    if([text isEqualToString:@"\n"]) {
        [textView resignFirstResponder];
        return NO;
    }
    
    return YES;
}

- (void)viewDidUnload {
    [self setTextView:nil];
    [super viewDidUnload];
}
- (IBAction)didClickSendButton:(id)sender {
    
    [self sendButtonClick];
}
@end
