//
//  AccountBookScrollView.m
//  BMSQS
//
//  Created by gh on 16/2/23.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "AccountBookScrollView.h"
#import "AccountListViewController.h"

@implementation AccountBookScrollView


- (id)initWithFrame:(CGRect)frame {
    
    self = [super initWithFrame:frame];
    if (self) {
        [self setViewUp];
    }
    return self;
    
}

- (void)setViewUp {
//    [self setViewLabel];
    [self setViewBtn];

    
}



//创建所有的label 消费支付等信息
- (void)setViewLabel:(id)data {
    NSDictionary *dic = data;
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 190)];
    view.backgroundColor = [UIColor whiteColor];
    [self addSubview:view];
    CGFloat originY = 10;
    
    for (int i=0; i<5; i++) {
        
        for (int j = 0; j<2; j++) {
            
            if (j == 0) {
                UILabel *label = [self ggSetLabel:CGRectMake(10, originY, APP_VIEW_WIDTH/2-10, 30) view:view];
                NSString *labelText;
                
                if (i == 4) {
                    label.frame = CGRectMake(10, originY, APP_VIEW_WIDTH - 10, 30);
                }
                
                switch (i) {
                    case 0:
                        labelText =  [NSString stringWithFormat:@"消费金额:%.2f元",[[dic objectForKey:@"consumptionAmount"] floatValue]];
                        
                        break;
                    case 1:
                        labelText = [NSString stringWithFormat:@"顾客支付:%.2f元",[[dic objectForKey:@"realPayAmount"] floatValue]];
                        
                        break;
                    case 2:
                        labelText = [NSString stringWithFormat:@"平台补贴:%.2f元",[[dic objectForKey:@"hqSubsidyAmount"] floatValue]];
                        break;
                    case 3:
                        labelText = [NSString stringWithFormat:@"本店让利:%.2f元",[[dic objectForKey:@"shopSubsidyAmount"] floatValue]];
                        break;
                    case 4:
                        labelText = [NSString stringWithFormat:@"已支付未消费:%.2f元",[[dic objectForKey:@"payedUnconsumedAmount"] floatValue]];
                        break;

                    default:
                        break;
                }
                label.text = labelText;
                label.tag = 2100 + i;
                
            }else {
                UILabel *label = [self ggSetLabel:CGRectMake(APP_VIEW_WIDTH/2, originY, APP_VIEW_WIDTH/2-5, 30) view:view];
                NSString *labelText;
                switch (i) {
                    case 0:
                        labelText = [NSString stringWithFormat:@"消费人次:%@",[dic objectForKey:@"consumptionCount"]];
                        break;
                    case 1:
                        labelText = [NSString stringWithFormat:@"其中未结算:%.2f元",[[dic objectForKey:@"realPayUnliquidatedAmount"] floatValue]];
                        break;
                    case 2:
                        labelText = [NSString stringWithFormat:@"其中未结算:%.2f元",[[dic objectForKey:@"hqSubsidyUnliquidatedAmount"] floatValue]];
                        break;
                    case 3:
                        labelText = [NSString stringWithFormat:@"顾客退款:%.2f元",[[dic objectForKey:@"refundAmount"] floatValue]];
                        break;
                    default:
                        break;
                }
                label.text = labelText;
                label.tag = 2200 + i;
            }
            
        }
        
        
        
        
        originY = originY + 30;
        
    }
    
    UILabel *label = [self ggSetLabel:CGRectMake(10, 150, APP_VIEW_WIDTH-20, 30) view:view];
    label.textAlignment = NSTextAlignmentRight;
    label.text = [NSString stringWithFormat:@"总计收入:%.2f元",[[dic objectForKey:@"incomeAmount"] floatValue]];
    label.tag = 2000;
    
    [self ggsetLineView:CGRectMake(0, 190-1, APP_VIEW_WIDTH, 1) view:view];
    
}

//创建所有的button
- (void)setViewBtn {
    CGFloat originY = 0;
    
    UIView *line = [[UIView alloc] initWithFrame:CGRectMake(0, 195, APP_VIEW_WIDTH, 10)];
    line.backgroundColor = [UIColor clearColor];
    [self addSubview:line];
    
    
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, line.frame.origin.y+line.frame.size.height, APP_VIEW_WIDTH, 200)];
    view.backgroundColor = [UIColor whiteColor];
    [self addSubview:view];
    
    for (int i=0; i<3; i++) {
        
        for (int j = 0; j<2; j++) {
            if (j == 0) {
                UIButton *button = [self ggSetBtn:CGRectMake(0, originY, APP_VIEW_WIDTH/2, 49) view:view tag:1100+i];
                button.tag = 1100+i;
                
            }else {
                UIButton *button = [self ggSetBtn:CGRectMake(APP_VIEW_WIDTH/2, originY, APP_VIEW_WIDTH/2, 49) view:view tag:1200+i];
                button.tag = 1200+i;
            }
            
        }
        
        originY = originY + 50;
        
    }
    
    UIButton *bottomBtn = [self ggSetBtn:CGRectMake(0, 150, APP_VIEW_WIDTH/2, 50) view:view tag:1000];
    
    bottomBtn.tag = 1000;
    
    for (int i=0; i<5; i++) {
        
        [self ggsetLineView:CGRectMake(0, 50*i-1, APP_VIEW_WIDTH, 1) view:view];
        
    }
    [self ggsetLineView:CGRectMake(APP_VIEW_WIDTH/2, 0, 1, 200) view:view];
    
}


//账单列表
- (void)btnAction :(UIButton *)btn {
    NSLog(@"%ld",(long)btn.tag);
    
    AccountListViewController *vc = [[AccountListViewController alloc] init];
    vc.vcTag = (int)btn.tag ;
    [[self viewController].navigationController pushViewController:vc animated:YES];
    
}


- (UIViewControllerEx *)viewController
{
    for (UIView* next = [self superview]; next; next = next.superview)
    {
        UIResponder *nextResponder = [next nextResponder];
        if ([nextResponder isKindOfClass:[UIViewController class]])
        {
            return (UIViewControllerEx *)nextResponder;
        }
    }
    return nil;
}



//创建button
- (UIButton *)ggSetBtn:(CGRect)frame view:(UIView *)view tag:(int)tag{
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    button.frame = frame;
    button.backgroundColor = [UIColor clearColor];
    [button addTarget:self action:@selector(btnAction:) forControlEvents:UIControlEventTouchUpInside];
    [view addSubview:button];
    UIImageView *iv = [[UIImageView alloc] init];
    iv.backgroundColor = [UIColor clearColor];
    [button addSubview:iv];
    
    
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(40, 0, button.frame.size.width-50, 50)];
    label.font = [UIFont systemFontOfSize:13.f];
    label.textColor = UICOLOR(49, 50, 51, 1);
    [button addSubview:label];
    
    NSString * btnTitle = @"";
    NSString * imageName;
    switch (tag) {
        case 1000:
            btnTitle = @"账单查询";
            imageName = @"bill_seven";
            break;
        case 1100:
            btnTitle = @"顾客清单";
            imageName = @"bill_one";
            break;
        case 1101:
            btnTitle =@"消费未结算账单";
            imageName = @"bill_three";
            break;
        case 1102:
            btnTitle = @"支付结算对账";
            imageName = @"bill_five";
            break;
        case 1200:
            btnTitle = @"退款清单";
            imageName = @"bill_two";
            break;
        case 1201:
            btnTitle = @"补贴未结算账单";
            imageName = @"bill_four";
            break;
        case 1202:
            btnTitle = @"补贴结算对账";
            imageName = @"bill_six";
            break;
        default:
            break;
    }
    iv.contentMode = UIViewContentModeScaleAspectFit;
    iv.frame = CGRectMake(10, 15, 20, 20);
    [iv setImage:[UIImage imageNamed:imageName]];
    label.text = btnTitle;
    
    return button;
}

- (void)ggsetLineView:(CGRect)frame view:(UIView *)view{
    UIView *lineView = [[UIView alloc] initWithFrame:frame];
    lineView.backgroundColor = UICOLOR(214, 215, 219, 1);
    [view addSubview:lineView];
    
}


//创建label
- (UILabel *)ggSetLabel:(CGRect)frame view:(UIView *)view{
    
    UILabel *label = [[UILabel alloc] initWithFrame:frame];
    label.textColor = UICOLOR(101, 102, 103, 1);
    label.font = [UIFont systemFontOfSize:13.f];
    [view addSubview:label];
    
    return label;
    
}


@end
