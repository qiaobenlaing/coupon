//
//  BMSQ_ClerkCell.m
//  BMSQS
//
//  Created by gh on 15/10/31.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_ClerkCell.h"

@interface BMSQ_ClerkCell ()<UIAlertViewDelegate> {
    
    UILabel *lb_top;
    UILabel *lb_bottom;
    
}

@end


@implementation BMSQ_ClerkCell


- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        
        [self setViewUP];
        
    }

    return self;
}

- (void)setViewUP {
    
    UILabel *lb_staff = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH, 44)];
    lb_staff.tag = 201;
    lb_staff.font = [UIFont systemFontOfSize:13];
    [self addSubview:lb_staff];
    
    _btnEdit = [UIButtonEx buttonWithType:UIButtonTypeCustom];
    _btnEdit.frame = CGRectMake(APP_VIEW_WIDTH-54, 0, 44, 44);
    [_btnEdit setBackgroundColor:[UIColor clearColor]];
    
    [_btnEdit addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];
    [self addSubview:_btnEdit];
    
    UIImageView *iv_edit = [[UIImageView alloc] initWithFrame:CGRectMake(10, 10, 24, 24)];
    iv_edit.image = [UIImage imageNamed:@"staff_edit_black"];
    [_btnEdit addSubview:iv_edit];
    
    _btnEdit.hidden = YES;
    
    
    UIView *line = [[UIView alloc] initWithFrame:CGRectMake(0, 43.5, APP_VIEW_WIDTH, 0.5)];
    line.backgroundColor = APP_CELL_LINE_COLOR;
    [self addSubview:line];
    
    //创建长按手势监听
    UILongPressGestureRecognizer *longPress = [[UILongPressGestureRecognizer alloc]
                                               initWithTarget:self
                                               action:@selector(myHandleTableviewCellLongPressed:)];
    
    longPress.delegate = self;
    longPress.minimumPressDuration = 1.0;
    //将长按手势添加到需要实现长按操作的视图里
    [self addGestureRecognizer:longPress];
    
    
    
}


- (void)setCellValue:(id)object {
    
    _btnEdit.object = object;
    UILabel *lb_staff = [self viewWithTag:201];
    
    lb_staff.text = [NSString stringWithFormat:@"%@:%@",[object objectForKey:@"realName"],[object objectForKey:@"mobileNbr"]];
}

- (void)btnAct:(UIButtonEx *)btn {
    
    if (self.delegate != nil) {
        [self.delegate cellBtnClick:btn.object];
    }
    
}

- (void)myHandleTableviewCellLongPressed:(UILongPressGestureRecognizer*)press{
    if (press.state == UIGestureRecognizerStateEnded) {
        
        return;
        
    } else if (press.state == UIGestureRecognizerStateBegan) {// "删除"
        
        //        NSLog(@"长按");
        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"确定要删除店员?" message:nil delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
        [alertView show];
        
        
        
        
    }
    
}

#pragma mark - UIAlertView delegate
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    if (buttonIndex == 1) {
        if (self.delegate != nil) {
            [self.delegate cellDelStaff:_btnEdit.object];
        }
        
    }
    
}



@end
