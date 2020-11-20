//
//  NewTopActivityCell.m
//  BMSQS
//
//  Created by gh on 15/12/29.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "NewTopActivityCell.h"


#define NEWTopCellColor clearColor

#define cellTextColor UICOLOR(204, 204, 204, 1);

#define cellHeight 45.0

@interface NewTopActivityCell () <UITextFieldDelegate>

@end


@implementation NewTopActivityCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        CGFloat y = 0;
        self.backgroundColor = UICOLOR(255, 255, 255, 1);
        
//        self.topImageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, y, APP_VIEW_WIDTH, 100)];
//        self.topImageView.backgroundColor = [UIColor whiteColor];
//        [self.contentView addSubview:self.topImageView];
//        y = y + self.topImageView.frame.size.height;
        
        
        
        
        self.textField1 = [[UITextField alloc] initWithFrame:CGRectMake(40, y, APP_VIEW_WIDTH-50, cellHeight)];
        self.textField1.tag = 1001;
        self.textField1.backgroundColor = [UIColor NEWTopCellColor];
        self.textField1.font = [UIFont systemFontOfSize:15.f];
        self.textField1.placeholder = @"活动标题（30个字内容）";
        [self.textField1 addTarget:self action:@selector(textFieldChange:) forControlEvents:UIControlEventEditingChanged];
        self.textField1.delegate = self;
        [self.contentView addSubview:self.textField1];
        
        UIImageView *imageView1 = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, 20, 20)];
        [imageView1 setImage:[UIImage imageNamed:@"活动标题"]];
        imageView1.center = CGPointMake(20, cellHeight/2);
        [self.contentView addSubview:imageView1];
        
        y = y+cellHeight;
        [self  DrawLineView:y];
        self.textField2 = [[UITextField alloc] initWithFrame:CGRectMake(40, y, APP_VIEW_WIDTH-50, cellHeight)];
        self.textField2.tag = 1002;
        self.textField2.backgroundColor = [UIColor NEWTopCellColor];
        self.textField2.font = [UIFont systemFontOfSize:15.f];
        self.textField2.placeholder = @"活动详细地址";
        
        [self.textField2 addTarget:self action:@selector(textFieldChange:) forControlEvents:UIControlEventEditingChanged];
        self.textField2.delegate = self;
        [self.contentView addSubview:self.textField2];
        
        UIImageView *imageView2 = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, 20, 20)];
        [imageView2 setImage:[UIImage imageNamed:@"活动地址"]];
        imageView2.center = CGPointMake(20, y + cellHeight/2);
        [self.contentView addSubview:imageView2];
        
        
        y = y+cellHeight;
        [self  DrawLineView:y];
        self.label3 = [[UILabel alloc] initWithFrame:CGRectMake(40, y, APP_VIEW_WIDTH-50, cellHeight)];
        self.label3.backgroundColor = [UIColor NEWTopCellColor];
        self.label3.font = [UIFont systemFontOfSize:15.f];
        self.label3.text = @"活动开始时间";
        self.label3.textColor = cellTextColor;
        [self.contentView addSubview:self.label3];

        UIImageView *imageView3 = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, 20, 20)];
        [imageView3 setImage:[UIImage imageNamed:@"时间"]];
        imageView3.center = CGPointMake(20, y + cellHeight/2);
        [self.contentView addSubview:imageView3];
        
        UIButton *button3 = [UIButton buttonWithType:UIButtonTypeCustom];
        button3.tag = 1003;
        button3.frame = self.label3.frame;
        button3.backgroundColor = [UIColor clearColor];
        [button3 setImage:[UIImage imageNamed:@"garyright"] forState:UIControlStateNormal];
        [button3 addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];
        button3.imageEdgeInsets = UIEdgeInsetsMake(0, button3.frame.size.width-10, 0, 0);
        [self.contentView addSubview:button3];
        
        y = y+cellHeight;
        [self  DrawLineView:y];
        self.label4 = [[UILabel alloc] initWithFrame:CGRectMake(40, y, APP_VIEW_WIDTH-50, cellHeight)];
        self.label4.backgroundColor = [UIColor NEWTopCellColor];
        self.label4.font = [UIFont systemFontOfSize:15.f];
        self.label4.text = @"活动结束时间（选填）";
        self.label4.textColor = cellTextColor;
        [self.contentView addSubview:self.label4];
        
        UIButton *button4 = [UIButton buttonWithType:UIButtonTypeCustom];
        button4.tag = 1004;
        button4.frame = self.label4.frame;
        button4.backgroundColor = [UIColor clearColor];
        [button4 setImage:[UIImage imageNamed:@"garyright"] forState:UIControlStateNormal];
        [button4 addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];
        button4.imageEdgeInsets = UIEdgeInsetsMake(0, button4.frame.size.width-10, 0, 0);
        [self.contentView addSubview:button4];
        
        y = y+cellHeight;
        [self DrawLineView:y];
        [self drawLabel:y title:@"活动类型"];
        
        self.button5 = [[UIButton alloc] initWithFrame:CGRectMake(10, y, APP_VIEW_WIDTH-20, cellHeight)];
        self.button5.tag = 1005;
        self.button5.backgroundColor = [UIColor clearColor];
        self.button5.titleLabel.font = [UIFont systemFontOfSize:13.f];
        [self.button5 setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
//        [self.button5 setTitle:@"活动类型" forState:UIControlStateNormal];
        self.button5.contentHorizontalAlignment = UIControlContentHorizontalAlignmentRight;
        [self.button5 setTitleEdgeInsets:UIEdgeInsetsMake(0, 0, 0, 20)];
        [self.button5 setImage:[UIImage imageNamed:@"garyright"] forState:UIControlStateNormal];
        self.button5.imageEdgeInsets = UIEdgeInsetsMake(0, self.button5.frame.size.width-10, 0, 0);
        [self.contentView addSubview:self.button5];
        [self.button5 addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];
        
        
        
        
        y = y+cellHeight;
        [self DrawLineView:y];
        [self drawLabel:y title:@"联系人"];
        self.textField6 = [[UITextField alloc] initWithFrame:CGRectMake(100, y, APP_VIEW_WIDTH-110, cellHeight)];
        self.textField6.tag = 1006;
        self.textField6.textAlignment = NSTextAlignmentRight;
        self.textField6.backgroundColor = [UIColor NEWTopCellColor];
        self.textField6.font = [UIFont systemFontOfSize:15.f];
        self.textField6.placeholder = @"请输入联系人";
        [self.textField6 addTarget:self action:@selector(textFieldChange:) forControlEvents:UIControlEventEditingChanged];
        self.textField6.delegate = self;
        [self.contentView addSubview:self.textField6];
        
        
        
        y = y+cellHeight;
        [self DrawLineView:y];
        [self drawLabel:y title:@"联系电话"];
        
        self.textField7 = [[UITextField alloc] initWithFrame:CGRectMake(100, y, APP_VIEW_WIDTH-110, cellHeight)];
        self.textField7.tag = 1007;
        self.textField7.textAlignment = NSTextAlignmentRight;
        self.textField7.backgroundColor = [UIColor NEWTopCellColor];
        self.textField7.font = [UIFont systemFontOfSize:15.f];
        self.textField7.keyboardType = UIKeyboardTypeNumberPad;
        self.textField7.placeholder = @"请输入联系电话";
        [self.textField7 addTarget:self action:@selector(textFieldChange:) forControlEvents:UIControlEventEditingChanged];
        self.textField7.delegate = self;
        [self.contentView addSubview:self.textField7];
        
        
    }
    
    return self;
}


- (void)btnAct:(UIButton *)sender {
    
    if (self.topActivityDelegate != nil) {
        
        if (sender.tag == 1003) { //开始时间
            [self.topActivityDelegate clickBeginTime];
            
        }else if (sender.tag == 1004) {
            [self.topActivityDelegate clickOverTime];
            
        }else if (sender.tag == 1005) {
            [self.topActivityDelegate gotoActivityType];
            
        }
        
        
    }

    
}


- (void)drawLabel:(CGFloat)labelY title:(NSString *)title {
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(10, labelY, APP_VIEW_WIDTH, cellHeight)];
    label.text = title;
    label.font = [UIFont systemFontOfSize:15.f];
    [self.contentView addSubview:label];
    
    
}


- (void)DrawLineView:(CGFloat)lineY {
    
    UIView *line = [[UIView alloc] initWithFrame:CGRectMake(0, lineY-0.5, APP_VIEW_WIDTH, 0.5)];
    line.backgroundColor = APP_VIEW_BACKCOLOR;
    [self.contentView addSubview:line];
    
}

- (void)textFieldChange:(UITextField *)textField {
    NSLog(@"------->%ld topCELL TEXT TAG",(long)textField.tag);
    if (self.topActivityDelegate != nil) {
        [self.topActivityDelegate topCellTextFieldChange:textField];
        
        
    }

}

- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField {
    
    
    if (self.topActivityDelegate != nil) {
        [self.topActivityDelegate topCellTag:textField];
    }
    return YES;
}

@end
