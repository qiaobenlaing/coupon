//
//  CourseCommentViewCell.m
//  BMSQS
//
//  Created by 新利软件－冯 on 16/3/16.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "CourseCommentViewCell.h"

@interface CourseCommentViewCell ()

@property (nonatomic, strong)UIView * iconView;
@property (nonatomic, strong)UIImageView * iconImage;//用户头像
@property (nonatomic, strong)UILabel     * userName;// 用户姓名
@property (nonatomic, strong)UILabel     * commentTime;//评论时间
@property (nonatomic, strong)UILabel     * grade;//打分
@property (nonatomic, strong)UIImageView     * starLevel1;// 一星
@property (nonatomic, strong)UIImageView     * starLevel2;// 二星
@property (nonatomic, strong)UIImageView     * starLevel3;// 三星
@property (nonatomic, strong)UIImageView     * starLevel4;// 四星
@property (nonatomic, strong)UIImageView     * starLevel5;// 五星
@property (nonatomic, strong)UILabel     * commentContent;//评论内容
@property (nonatomic, strong)RemarkImgView * remarkImgView;// 评论图片


@end

@implementation CourseCommentViewCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        
        [self setCellParentComment];
    }
    return self;
}

- (void)setCellParentComment
{
    
    self.userName = [[UILabel alloc] initWithFrame:CGRectMake(10, 10, APP_VIEW_WIDTH - 200, 15)];
    self.userName.font = [UIFont systemFontOfSize:14.0];
    self.userName.text = @"用户昵称";
    [self addSubview:self.userName];
    
    self.commentTime = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH - 90, 32, 90, 15)];
    self.commentTime.font = [UIFont systemFontOfSize:12.0];
    self.commentTime.text = @"2月18日";
    [self addSubview:self.commentTime];
    
    self.grade = [[UILabel alloc] initWithFrame:CGRectMake(10, 32, 25, 15)];
    self.grade.font = [UIFont systemFontOfSize:11.0];
    self.grade.text = @"打分";
    [self addSubview:self.grade];
    
    self.starLevel1 = [[UIImageView alloc] initWithFrame:CGRectMake(35, 35, 10, 10)];
    self.starLevel1.image = [UIImage imageNamed:@"share"];
    self.starLevel1.hidden = YES;
    [self addSubview:self.starLevel1];
    
    self.starLevel2 = [[UIImageView alloc] initWithFrame:CGRectMake(47, 35, 10, 10)];
    self.starLevel2.image = [UIImage imageNamed:@"share"];
    self.starLevel2.hidden = YES;
    [self addSubview:self.starLevel2];
    
    self.starLevel3 = [[UIImageView alloc] initWithFrame:CGRectMake(59, 35, 10, 10)];
    self.starLevel3.image = [UIImage imageNamed:@"share"];
    self.starLevel3.hidden = YES;
    [self addSubview:self.starLevel3];
    
    self.starLevel4 = [[UIImageView alloc] initWithFrame:CGRectMake(71, 35, 10, 10)];
    self.starLevel4.image = [UIImage imageNamed:@"share"];
    self.starLevel4.hidden = YES;
    [self addSubview:self.starLevel4];
    
    self.starLevel5 = [[UIImageView alloc] initWithFrame:CGRectMake(83, 35, 10, 10)];
    self.starLevel5.image = [UIImage imageNamed:@"share"];
    self.starLevel5.hidden = YES;
    [self addSubview:self.starLevel5];
    
    self.commentContent = [[UILabel alloc] initWithFrame:CGRectMake(10, 55, APP_VIEW_WIDTH -  20, 20)];
    self.commentContent.font = [UIFont systemFontOfSize:12.0];
    self.commentContent.numberOfLines = 0;
    self.commentContent.backgroundColor = [UIColor purpleColor];
    [self addSubview:self.commentContent];
    
    self.remarkImgView = [[RemarkImgView alloc] initWithFrame:CGRectMake(0, 60 + self.commentContent.frame.size.height, APP_VIEW_WIDTH, 120)];
    self.remarkImgView.backgroundColor = [UIColor clearColor]; // 横向滑动的视图
    [self addSubview:self.remarkImgView];
    
    
    
    
}

- (void)setCellCourseCommentDic:(NSDictionary *)commentDic
{
    self.userName.text = [NSString stringWithFormat:@"%@", commentDic[@"nickName"]];
    
    NSString * MontStr = [commentDic[@"remarkTime"] substringWithRange:NSMakeRange(5, 2)];
    NSString * DayStr = [commentDic[@"remarkTime"] substringWithRange:NSMakeRange(8, 2)];
    self.commentTime.text = [NSString stringWithFormat:@"%@月%@日",MontStr ,DayStr];
    
    NSString *  wholeLvl = [NSString stringWithFormat:@"%@", commentDic[@"wholeLvl"]];
    if (wholeLvl.intValue == 1) {
        self.starLevel1.hidden = NO;
    }else if (wholeLvl.intValue == 2){
        self.starLevel1.hidden = NO;
        self.starLevel2.hidden = NO;
    }else if (wholeLvl.intValue == 3){
        self.starLevel1.hidden = NO;
        self.starLevel2.hidden = NO;
        self.starLevel3.hidden = NO;
    }else if (wholeLvl.intValue == 4){
        self.starLevel1.hidden = NO;
        self.starLevel2.hidden = NO;
        self.starLevel3.hidden = NO;
        self.starLevel4.hidden = NO;
    }else if (wholeLvl.intValue == 5){
        self.starLevel1.hidden = NO;
        self.starLevel2.hidden = NO;
        self.starLevel3.hidden = NO;
        self.starLevel4.hidden = NO;
        self.starLevel5.hidden = NO;
    }
    
    self.commentContent.text = [NSString stringWithFormat:@"%@", commentDic[@"remark"]];
    CGSize size = [self.commentContent.text boundingRectWithSize:CGSizeMake(self.commentContent.frame.size.width, MAXFLOAT) options:NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName: self.commentContent.font} context:nil].size;
    self.commentContent.frame = CGRectMake(10, 55, APP_VIEW_WIDTH -  20, size.height);
    self.remarkImgView.frame = CGRectMake(0, 60 + self.commentContent.frame.size.height, APP_VIEW_WIDTH, 120);
    
    if ([commentDic[@"classRemarkImg"] count] == 0) {
        self.remarkImgView.hidden = YES;
    }else{
        [self.remarkImgView setRemarkWithAry:commentDic[@"classRemarkImg"]];
    }
    
    
    
    
    
}


@end
